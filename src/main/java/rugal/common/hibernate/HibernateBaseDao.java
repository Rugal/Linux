/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.common.hibernate;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import rugal.common.util.BeanUtils;

/**
 *
 * @author Rugal Bernstein
 */
@Transactional
public abstract class HibernateBaseDao<T, ID extends Serializable> extends HibernateSimpleDao
{

    /**
     * @see Session.get(Class,Serializable)
     * @param id
     * <p/>
     * @return 持久化对象
     */
    protected T get(ID id)
    {
        return get(id, false);
    }

    /**
     * @see Session.get(Class,Serializable,LockMode)
     * @param id 对象ID
     * @param lock 是否锁定，使用LockMode.UPGRADE
     * <p/>
     * @return 持久化对象
     */
    protected T get(ID id, boolean lock)
    {
        T entity;
        if (lock) {
            entity = (T) getSession().get(getEntityClass(), id,
                    LockMode.UPGRADE);
        } else {
            entity = (T) getSession().get(getEntityClass(), id);
        }
        return entity;
    }

    /**
     * 按属性查找对象列表
     */
    protected List<T> findByProperty(String property, Object value)
    {
        Assert.hasText(property);
        return createCriteria(Restrictions.eq(property, value)).list();
    }

    protected List<T> findByPropertyBefore(String property, Object value)
    {
        Assert.hasText(property);
        return createCriteria(Restrictions.like(property, value + "%")).list();
    }

    protected List<T> findByPropertyVague(String property, Object value)
    {
        Assert.hasText(property);
        return createCriteria(Restrictions.like(property, "%" + value + "%")).list();
    }

    protected List<T> findByPropertyAfter(String property, Object value)
    {
        Assert.hasText(property);
        return createCriteria(Restrictions.like(property, "%" + value)).list();
    }

    /**
     * 按属性查找唯一对象
     */
    protected T findUniqueByProperty(String property, Object value)
    {
        Assert.hasText(property);
        Assert.notNull(value);
        return (T) createCriteria(Restrictions.eq(property, value)).uniqueResult();
    }

    /**
     * 按属性统计记录数
     *
     * @param property
     * @param value
     * <p/>
     * @return
     */
    protected int countByProperty(String property, Object value)
    {
        Assert.hasText(property);
        Assert.notNull(value);
        return ((Number) (createCriteria(
                Restrictions.eq(property, value)).setProjection(
                Projections.rowCount()).uniqueResult())).intValue();
    }

    /**
     * 按Criterion查询列表数据.
     *
     * @param criterion 数量可变的Criterion.
     */
    protected List findByCriteria(Criterion... criterion)
    {
        return createCriteria(criterion).list();
    }

    /**
     * 通过Updater更新对象
     *
     * @param updater
     * <p/>
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public T updateByUpdater(Updater<T> updater)
    {
        ClassMetadata cm = sessionFactory.getClassMetadata(
                getEntityClass());
        T bean = updater.getBean();
        T po = (T) getSession().get(getEntityClass(),
                cm.getIdentifier(bean));
        updaterCopyToPersistentObject(updater, po, cm);
        return po;
    }

    /**
     * 将更新对象拷贝至实体对象，并处理many-to-one的更新。
     *
     * @param updater
     * @param po
     */
    private void updaterCopyToPersistentObject(Updater<T> updater, T po,
            ClassMetadata cm)
    {
        String[] propNames = cm.getPropertyNames();
        String identifierName = cm.getIdentifierPropertyName();
        T bean = updater.getBean();
        Object value;
        for (String propName : propNames) {
            if (propName.equals(identifierName)) {
                continue;
            }
            try {
                value = BeanUtils.getSimpleProperty(bean,
                        propName);
                if (!updater.isUpdate(propName, value)) {
                    continue;
                }
                cm.setPropertyValue(po, propName, value);
            } catch (Exception e) {
                throw new RuntimeException(
                        "copy property to persistent object failed: '"
                        + propName + "'", e);
            }
        }
    }

    /**
     * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数.
     */
    protected Criteria createCriteria(Criterion... criterions)
    {
        Criteria criteria = getSession().createCriteria(getEntityClass());
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        return criteria;
    }

    /**
     * 获得Dao对于的实体类
     *
     * @return
     */
    abstract protected Class<T> getEntityClass();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.food.dao.impl;

import java.util.Date;
import rugal.food.dao.IndentDao;
import org.hibernate.Criteria;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rugal.common.hibernate.HibernateBaseDao;
import rugal.common.page.Pagination;
import rugal.food.entity.Indent;

/**
 *
 * @author Administrator
 */
public class IndentDaoImpl extends HibernateBaseDao<Indent, Integer> implements IndentDao
{

    @Override
    public Pagination getPage(int pageNo, int pageSize)
    {
        Criteria crit = createCriteria();
        Pagination page = findByCriteria(crit, pageNo, pageSize);
        return page;
    }

    @Override
    public Indent findById(Integer id)
    {
        Indent entity = get(id);
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Indent save(Indent bean)
    {
        bean.setOrderTime(new Date().getTime());
        getSession().save(bean);
        return bean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Indent deleteById(Integer id)
    {
        Indent entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }

    @Override
    protected Class<Indent> getEntityClass()
    {
        return Indent.class;
    }
}

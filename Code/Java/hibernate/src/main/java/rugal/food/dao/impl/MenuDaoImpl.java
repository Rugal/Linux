/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.food.dao.impl;

import rugal.food.dao.MenuDao;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rugal.common.hibernate.HibernateBaseDao;
import rugal.common.page.Pagination;
import rugal.food.entity.Menu;

/**
 *
 * @author Administrator
 */
@Repository
public class MenuDaoImpl extends HibernateBaseDao<Menu, Integer> implements MenuDao
{
    
    @Override
    public Pagination getPage(int pageNo, int pageSize)
    {
        Criteria crit = createCriteria();
        Pagination page = findByCriteria(crit, pageNo, pageSize);
        return page;
    }
    
    @Override
    public List<Menu> findByName(String name)
    {
        return super.findByProperty("name", name);
    }
    
    @Override
    public List<Menu> findByVagueName(String name)
    {
        return super.findByPropertyVague("name", name);
    }

    @Override
    public Menu findById(Integer id)
    {
        Menu entity = get(id);
        if (null != entity) {
            //set average duration for a menu
            String hql = "select avg(bean.duration) from OrderLog bean where bean.mid=:mid";
            Query query = getSession().createQuery(hql);
            query.setParameter("mid", id);
            entity.setDuration(((Float) query.iterate().next()).floatValue());
            //set average quality for a menu
            hql = "select avg(bean.quality) from OrderLog bean where bean.mid=:mid";
            query.setParameter("mid", id);
            query = getSession().createQuery(hql);
            entity.setQuality(((Float) query.iterate().next()).floatValue());
        }
        return entity;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Menu save(Menu bean)
    {
        getSession().save(bean);
        return bean;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Menu deleteById(Integer id)
    {
        Menu entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }
    
    @Override
    protected Class<Menu> getEntityClass()
    {
        return Menu.class;
    }
}

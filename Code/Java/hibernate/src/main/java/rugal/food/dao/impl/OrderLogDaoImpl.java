/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.food.dao.impl;

import rugal.food.dao.OrderLogDao;
import org.hibernate.Criteria;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rugal.common.hibernate.HibernateBaseDao;
import rugal.common.page.Pagination;
import rugal.food.entity.OrderLog;

/**
 *
 * @author Administrator
 */
public class OrderLogDaoImpl extends HibernateBaseDao<OrderLog, Integer> implements OrderLogDao
{
    
    @Override
    public Pagination getPage(int pageNo, int pageSize)
    {
        Criteria crit = createCriteria();
        Pagination page = findByCriteria(crit, pageNo, pageSize);
        return page;
    }

    @Override
    public OrderLog findById(Integer id)
    {
        OrderLog entity = get(id);
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderLog save(OrderLog bean)
    {
        getSession().save(bean);
        return bean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderLog deleteById(Integer id)
    {
        OrderLog entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }

    @Override
    protected Class<OrderLog> getEntityClass()
    {
        return OrderLog.class;
    }
}

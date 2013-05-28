/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.food.dao.impl;

import java.util.Date;
import rugal.food.dao.UserDao;
import java.util.List;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rugal.common.hibernate.HibernateBaseDao;
import rugal.common.page.Pagination;
import rugal.food.entity.User;

/**
 *
 * @author Administrator
 */
@Repository
public class UserDaoImpl extends HibernateBaseDao<User, String> implements UserDao
{

    @Override
    public Pagination getPage(int pageNo, int pageSize)
    {
        Criteria crit = createCriteria();
        Pagination page = findByCriteria(crit, pageNo, pageSize);
        return page;
    }

    @Override
    public User findById(String id)
    {
        User entity = get(id);
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User save(User bean)
    {
        bean.setLastLogin(new Date().getTime());
        getSession().save(bean);
        return bean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User deleteById(String id)
    {
        User entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }

    @Override
    protected Class<User> getEntityClass()
    {
        return User.class;
    }
}

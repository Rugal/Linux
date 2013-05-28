/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.food.dao.impl;

import rugal.food.dao.RestaurantDao;
import java.util.List;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rugal.common.hibernate.Finder;
import rugal.common.hibernate.HibernateBaseDao;
import rugal.common.page.Pagination;
import rugal.food.entity.Restaurant;

/**
 *
 * @author Rugal
 */
@Repository
//@Transactional
public class RestaurantDaoImpl extends HibernateBaseDao<Restaurant, Integer> implements RestaurantDao
{

    @Override
    public Pagination getPage(int pageNo, int pageSize)
    {
        Criteria crit = createCriteria();
        Pagination page = findByCriteria(crit, pageNo, pageSize);
        return page;
    }

//    @Override//needs fix
//    public Pagination getPageByScore(Integer scores, int pageNo, int pageSize)
//    {
//        String hql = "SELECT bean FROM Restaurant bean group by ";
//        Finder f = Finder.create(hql);
//        if (null != scores)
//        {
//            f.append(" where (score/scored_times) > :scores");
////            f.append(" where (score/scored_times) > " + scores);
//            f.setParam("scores", scores);
//        }
//        f.append(" order by score/scored_times desc");
//        Pagination page = find(f, pageNo, pageSize);
//        return page;
//    }
    @Override
    public List<Restaurant> findByName(String name)
    {
        return super.findByProperty("name", name);
    }

    @Override
    public List<Restaurant> findByVagueName(String name)
    {
        return super.findByPropertyVague("name", name);
    }

    @Override
    public Restaurant findById(Integer id)
    {
        Restaurant entity = get(id);
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Restaurant save(Restaurant bean)
    {
        getSession().save(bean);
        return bean;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Restaurant deleteById(Integer id)
    {
        Restaurant entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }

    @Override
    protected Class<Restaurant> getEntityClass()
    {
        return Restaurant.class;
    }
}

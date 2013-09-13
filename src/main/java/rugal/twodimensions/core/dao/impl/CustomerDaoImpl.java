/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import rugal.common.hibernate.HibernateBaseDao;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.CustomerDao;
import rugal.twodimensions.core.entity.Customer;

/**
 *
 * @author Administrator
 */
@Repository
public class CustomerDaoImpl extends HibernateBaseDao<Customer, Integer> implements CustomerDao {

    @Override
    public Pagination getPage(int pageNo, int pageSize) {
        Criteria crit = createCriteria();
        Pagination page = findByCriteria(crit, pageNo, pageSize);
        return page;
    }

    @Override
    public Customer findById(Integer id) {
        Customer entity = get(id);
        return entity;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public Customer save(Customer bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public Customer deleteById(Integer id) {
        Customer entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }

    @Override
    protected Class<Customer> getEntityClass() {
        return Customer.class;
    }
}
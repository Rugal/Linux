/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import rugal.common.hibernate.HibernateBaseDao;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.VendorDao;
import rugal.twodimensions.core.entity.Vendor;

/**
 *
 * @author rugal
 */
@Repository
public class VendorDaoImpl extends HibernateBaseDao<Vendor, Integer> implements VendorDao  {

    @Override
    public Pagination getPage(int pageNo, int pageSize) {
        Criteria crit = createCriteria();
        Pagination page = findByCriteria(crit, pageNo, pageSize);
        return page;
    }

    @Override
    public Vendor findById(Integer id) {
        Vendor entity = get(id);
        return entity;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public Vendor save(Vendor bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public Vendor deleteById(Integer id) {
        Vendor entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }

    @Override
    protected Class<Vendor> getEntityClass() {
        return Vendor.class;
    }
}

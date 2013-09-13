/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import rugal.common.hibernate.HibernateBaseDao;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.VenditionLogDao;
import rugal.twodimensions.core.entity.VenditionLog;

/**
 *
 * @author rugal
 */
@Repository
public class VenditionLogDaoImpl extends HibernateBaseDao<VenditionLog, Integer> implements VenditionLogDao {

    @Override
    public Pagination getPage(int pageNo, int pageSize) {
        Criteria crit = createCriteria();
        Pagination page = findByCriteria(crit, pageNo, pageSize);
        return page;
    }

    @Override
    public VenditionLog findById(Integer id) {
        VenditionLog entity = get(id);
        return entity;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public VenditionLog save(VenditionLog bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public VenditionLog deleteById(Integer id) {
        VenditionLog entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }

    @Override
    protected Class<VenditionLog> getEntityClass() {
        return VenditionLog.class;
    }
}

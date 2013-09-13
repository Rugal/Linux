/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import rugal.common.hibernate.HibernateBaseDao;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.StockLogDao;
import rugal.twodimensions.core.entity.StockLog;

/**
 *
 * @author rugal
 */
@Repository
public class StockLogDaoImpl extends HibernateBaseDao<StockLog, Integer> implements StockLogDao {

    @Override
    public Pagination getPage(int pageNo, int pageSize) {
        Criteria crit = createCriteria();
        Pagination page = findByCriteria(crit, pageNo, pageSize);
        return page;
    }

    @Override
    public StockLog findById(Integer id) {
        StockLog entity = get(id);
        return entity;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public StockLog save(StockLog bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public StockLog deleteById(Integer id) {
        StockLog entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }

    @Override
    protected Class<StockLog> getEntityClass() {
        return StockLog.class;
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
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
        crit.addOrder(Order.desc("log_time"));
        Pagination page = findByCriteria(crit, pageNo, pageSize);
        return page;
    }

    @Override
    public Pagination getPageByGoods(boolean asc, int pageNo, int pageSize) {
//        String hql = "SELECT bean.gid,sum(bean.quantity) FROM VenditionLog bean GROUP BY bean.gid ORDER BY :order";
//        Query query = getSession().createQuery(hql);
//        query.setParameter("order", (asc ? "asc" : "desc"));
//        query.list();
        Criteria crit = createCriteria();
        crit.setProjection(Projections.projectionList()
                .add(Projections.alias(Projections.groupProperty("gid"), "goods_number"))
                .add(Projections.sum("quantity").as("total")));
        Order o = asc ? Order.asc("total") : Order.desc("total");
        crit.addOrder(o);
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

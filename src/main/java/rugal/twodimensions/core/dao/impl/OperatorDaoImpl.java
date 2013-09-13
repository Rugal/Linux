/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import rugal.common.hibernate.HibernateBaseDao;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.OperatorDao;
import rugal.twodimensions.core.entity.Operator;

/**
 *
 * @author rugal
 */
@Repository
public class OperatorDaoImpl extends HibernateBaseDao<Operator, Integer> implements OperatorDao {

    @Override
    public Pagination getPage(int pageNo, int pageSize) {
        Criteria crit = createCriteria();
        Pagination page = findByCriteria(crit, pageNo, pageSize);
        return page;
    }

    @Override
    public Operator findById(Integer id) {
        Operator entity = get(id);
        return entity;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public Operator save(Operator bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public Operator deleteById(Integer id) {
        Operator entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }

    @Override
    protected Class<Operator> getEntityClass() {
        return Operator.class;
    }
}
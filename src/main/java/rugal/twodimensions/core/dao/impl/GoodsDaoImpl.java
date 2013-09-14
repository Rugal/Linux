/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import rugal.common.hibernate.HibernateBaseDao;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.GoodsDao;
import rugal.twodimensions.core.entity.Goods;

/**
 *
 * @author rugal
 */
@Repository
public class GoodsDaoImpl extends HibernateBaseDao<Goods, Integer> implements GoodsDao {

    @Override
    public Pagination getPage(int pageNo, int pageSize) {
        Criteria crit = createCriteria();
        Pagination page = findByCriteria(crit, pageNo, pageSize);
        return page;
    }

    public List<Goods> findByName(String name) {
        return super.findByPropertyVague("name", name);
    }

    @Override
    public Goods findById(Integer id) {
        Goods entity = get(id);
        return entity;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public Goods save(Goods bean) {
        getSession().save(bean);
        return bean;
    }

    @Override
//    @Transactional(propagation = Propagation.REQUIRED)
    public Goods deleteById(Integer id) {
        Goods entity = super.get(id);
        if (entity != null) {
            getSession().delete(entity);
        }
        return entity;
    }

    @Override
    protected Class<Goods> getEntityClass() {
        return Goods.class;
    }
}
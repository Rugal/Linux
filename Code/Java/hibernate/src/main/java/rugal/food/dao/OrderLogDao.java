/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.food.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rugal.common.hibernate.Updater;
import rugal.common.page.Pagination;
import rugal.food.entity.OrderLog;

/**
 *
 * @author Administrator
 */
public interface OrderLogDao
{

    @Transactional(propagation = Propagation.REQUIRED)
    OrderLog deleteById(Integer id);

    OrderLog findById(Integer id);

    Pagination getPage(int pageNo, int pageSize);

    @Transactional(propagation = Propagation.REQUIRED)
    OrderLog save(OrderLog bean);

    @Transactional(propagation = Propagation.REQUIRED)
    OrderLog updateByUpdater(Updater<OrderLog> updater);
}

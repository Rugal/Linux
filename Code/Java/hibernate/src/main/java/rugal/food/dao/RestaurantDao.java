/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.food.dao;

import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rugal.common.hibernate.Updater;
import rugal.common.page.Pagination;
import rugal.food.entity.Restaurant;

/**
 *
 * @author Rugal
 */
public interface RestaurantDao
{

    @Transactional(propagation = Propagation.REQUIRED)
    Restaurant deleteById(Integer id);

    Restaurant findById(Integer id);

    List<Restaurant> findByName(String name);

    List<Restaurant> findByVagueName(String name);

    Pagination getPage(int pageNo, int pageSize);

    @Transactional(propagation = Propagation.REQUIRED)
    Restaurant save(Restaurant bean);

    @Transactional(propagation = Propagation.REQUIRED)
    Restaurant updateByUpdater(Updater<Restaurant> updater);
}

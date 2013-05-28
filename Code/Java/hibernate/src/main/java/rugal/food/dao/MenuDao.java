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
import rugal.food.entity.Menu;

/**
 *
 * @author Rugal Bernstein
 */
public interface MenuDao
{

    @Transactional(propagation = Propagation.REQUIRED)
    Menu deleteById(Integer id);

    Menu findById(Integer id);

    List<Menu> findByName(String name);

    List<Menu> findByVagueName(String name);

    Pagination getPage(int pageNo, int pageSize);

    @Transactional(propagation = Propagation.REQUIRED)
    Menu save(Menu bean);

    @Transactional(propagation = Propagation.REQUIRED)
    Menu updateByUpdater(Updater<Menu> updater);
}

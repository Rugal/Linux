/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.food.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rugal.common.hibernate.Updater;
import rugal.common.page.Pagination;
import rugal.food.entity.User;

/**
 *
 * @author Administrator
 */
public interface UserDao
{

    @Transactional(propagation = Propagation.REQUIRED)
    User deleteById(String id);

    User findById(String id);

    Pagination getPage(int pageNo, int pageSize);

    @Transactional(propagation = Propagation.REQUIRED)
    User save(User bean);

    @Transactional(propagation = Propagation.REQUIRED)
    User updateByUpdater(Updater<User> updater);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.food.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rugal.common.hibernate.Updater;
import rugal.common.page.Pagination;
import rugal.food.entity.Indent;

/**
 *
 * @author Administrator
 */
public interface IndentDao
{

    @Transactional(propagation = Propagation.REQUIRED)
    Indent deleteById(Integer id);

    Indent findById(Integer id);

    Pagination getPage(int pageNo, int pageSize);

    @Transactional(propagation = Propagation.REQUIRED)
    Indent save(Indent bean);

    @Transactional(propagation = Propagation.REQUIRED)
    Indent updateByUpdater(Updater<Indent> updater);
}

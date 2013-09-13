/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao;

import rugal.common.hibernate.Updater;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.entity.Operator;

/**
 *
 * @author rugal
 */
public interface OperatorDao {

    Operator deleteById(Integer id);

    Operator findById(Integer id);

    Pagination getPage(int pageNo, int pageSize);

    Operator save(Operator bean);

    Operator updateByUpdater(Updater<Operator> updater);
}

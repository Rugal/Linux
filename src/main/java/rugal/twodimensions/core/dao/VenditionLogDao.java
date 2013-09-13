/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao;

import rugal.common.hibernate.Updater;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.entity.VenditionLog;

/**
 *
 * @author rugal
 */
public interface VenditionLogDao {

    VenditionLog deleteById(Integer id);

    VenditionLog findById(Integer id);

    Pagination getPage(int pageNo, int pageSize);

    VenditionLog save(VenditionLog bean);

    VenditionLog updateByUpdater(Updater<VenditionLog> updater);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service;

import rugal.common.page.Pagination;
import rugal.twodimensions.core.entity.VenditionLog;

/**
 *
 * @author rugal
 */
public interface VenditionLogService {

    VenditionLog deleteById(Integer id);

    Pagination getPageByGoods(boolean asc, int pageNo, int pageSize);

    VenditionLog findById(Integer id);

    Pagination getPage(int pageNo, int pageSize);

    VenditionLog save(VenditionLog bean);
}

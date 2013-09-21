/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service;

import rugal.common.page.Pagination;
import rugal.twodimensions.core.entity.StockLog;

/**
 *
 * @author rugal
 */
public interface StockLogService {

//    StockLog deleteById(Integer id);
    StockLog findById(Integer id);

    Pagination getPage(boolean desc, int pageNo, int pageSize);
//    StockLog save(StockLog bean);
}

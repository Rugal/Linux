/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service.impl;

import rugal.twodimensions.core.service.StockLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.StockLogDao;
import rugal.twodimensions.core.entity.StockLog;

/**
 *
 * @author rugal
 */
@Service
public class StockLogServiceImpl implements StockLogService {

    @Autowired
    private StockLogDao stockLogDao;

    @Override
    public StockLog deleteById(Integer id) {
        return stockLogDao.deleteById(id);
    }

    @Override
    public StockLog findById(Integer id) {
        return stockLogDao.findById(id);
    }

    @Override
    public Pagination getPage(int pageNo, int pageSize) {
        return stockLogDao.getPage(pageNo, pageSize);
    }

    @Override
    public StockLog save(StockLog bean) {
        return stockLogDao.save(bean);
    }
}

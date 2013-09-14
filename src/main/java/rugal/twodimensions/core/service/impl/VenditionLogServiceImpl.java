/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.VenditionLogDao;
import rugal.twodimensions.core.entity.VenditionLog;
import rugal.twodimensions.core.service.VenditionLogService;

/**
 *
 * @author rugal
 */
public class VenditionLogServiceImpl implements VenditionLogService {

    @Autowired
    private VenditionLogDao dao;

    @Override
    public VenditionLog deleteById(Integer id) {
        return dao.deleteById(id);
    }

    public Pagination getPageByGoods(boolean asc, int pageNo, int pageSize) {
        return dao.getPageByGoods(asc, pageNo, pageSize);
    }

    @Override
    public VenditionLog findById(Integer id) {
        return dao.findById(id);
    }

    @Override
    public Pagination getPage(int pageNo, int pageSize) {
        return dao.getPage(pageNo, pageSize);
    }

    @Override
    public VenditionLog save(VenditionLog bean) {
        return dao.save(bean);
    }
}

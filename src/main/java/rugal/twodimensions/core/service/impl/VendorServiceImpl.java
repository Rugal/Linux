/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.VendorDao;
import rugal.twodimensions.core.entity.Vendor;
import rugal.twodimensions.core.service.VendorService;

/**
 *
 * @author rugal
 */
@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorDao vendorDao;

    @Override
    public Vendor deleteById(Integer id) {
        return vendorDao.deleteById(id);
    }

    @Override
    public Vendor findById(Integer id) {
        return vendorDao.findById(id);
    }

    @Override
    public Pagination getPage(int pageNo, int pageSize) {
        return vendorDao.getPage(pageNo, pageSize);
    }

    @Override
    public Vendor save(Vendor bean) {
        return vendorDao.save(bean);
    }
}

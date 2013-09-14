/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service;

import rugal.common.page.Pagination;
import rugal.twodimensions.core.entity.Vendor;

/**
 *
 * @author rugal
 */
public interface VendorService {

    Vendor deleteById(Integer id);

    Vendor findById(Integer id);

    Pagination getPage(int pageNo, int pageSize);

    Vendor save(Vendor bean);
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service;

import rugal.common.page.Pagination;
import rugal.twodimensions.core.entity.Customer;

/**
 *
 * @author rugal
 */
public interface CustomerService {

    Customer activate(Customer bean);

    Customer deleteById(Integer id);

    Customer findById(Integer id);

    Pagination getPage(int pageNo, int pageSize);

    Customer save(Customer bean);

    Customer updateCredit(Customer bean, Integer num);
    
}

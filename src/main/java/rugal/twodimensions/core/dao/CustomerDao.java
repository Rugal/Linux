/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao;

import rugal.common.hibernate.Updater;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.entity.Customer;

/**
 *
 * @author Administrator
 */
public interface CustomerDao {

    Customer findById(Integer id);

    Pagination getPage(int pageNo, int pageSize);

    Customer deleteById(Integer id);

    Customer save(Customer bean);

    Customer updateByUpdater(Updater<Customer> updater);
}

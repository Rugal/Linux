/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import rugal.common.hibernate.Updater;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.CustomerDao;
import rugal.twodimensions.core.entity.Customer;
import rugal.twodimensions.core.service.CustomerService;

/**
 *
 * @author rugal
 */
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public Customer findById(Integer id) {
        return customerDao.findById(id);
    }

    @Override
    public Pagination getPage(int pageNo, int pageSize) {
        return customerDao.getPage(pageNo, pageSize);
    }

    @Override
    public Customer deleteById(Integer id) {
        return customerDao.deleteById(id);
    }

    @Override
    public Customer save(Customer bean) {
        return customerDao.save(bean);
    }

    @Override
    public Customer activate(Customer bean) {
        Updater<Customer> updater = new Updater<Customer>(bean, Updater.UpdateMode.MIN);
        updater.include("membership");
        bean.setMembership(1);
        return customerDao.updateByUpdater(updater);
    }

    @Override
    public Customer updateCredit(Customer bean, Integer num) {
        Updater<Customer> updater = new Updater<Customer>(bean, Updater.UpdateMode.MIN);
        updater.include("credit");
        bean.changeCredit(num);
        return customerDao.updateByUpdater(updater);
    }
}

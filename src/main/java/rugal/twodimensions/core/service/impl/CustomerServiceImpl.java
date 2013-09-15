/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rugal.common.hibernate.Updater;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.CustomerDao;
import rugal.twodimensions.core.entity.Customer;
import rugal.twodimensions.core.service.CustomerService;

/**
 *
 * @author rugal
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao dao;

    @Override
    public Customer findById(Integer id) {
        return dao.findById(id);
    }

    @Override
    public Pagination getPage(int pageNo, int pageSize) {
        return dao.getPage(pageNo, pageSize);
    }

    @Override
    public Customer deleteById(Integer id) {
        return dao.deleteById(id);
    }

    @Override
    public Customer save(Customer bean) {
        return dao.save(bean);
    }

    @Override
    public Customer activate(Customer bean) {
        Updater<Customer> updater = new Updater<Customer>(bean, Updater.UpdateMode.MIN);
        updater.include("membership");
        bean.setMembership(1);
        return dao.updateByUpdater(updater);
    }

    @Override
    public Customer updateCredit(Customer bean, Integer num) {
        Updater<Customer> updater = new Updater<Customer>(bean, Updater.UpdateMode.MIN);
        updater.include("credit");
        bean.changeCredit(num);
        return dao.updateByUpdater(updater);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service.impl;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.entity.Customer;
import rugal.twodimensions.core.service.CustomerService;

/**
 *
 * @author rugal
 */
public class CustomerServiceImplTest {

    private static CustomerService customerServiceImpl;

    public CustomerServiceImplTest() {
    }

    @Before
    public void setUp() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        customerServiceImpl = (CustomerService) ctx.getBean("customerServiceImpl");
    }

    @After
    public void tearDown() {
    }

//    @Test
//    public void testFindById() {
//        System.out.println("findById");
//        Integer id = null;
//        CustomerServiceImpl instance = new CustomerServiceImpl();
//        Customer expResult = null;
//        Customer result = instance.findById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testGetPage() {
//        System.out.println("getPage");
//        int pageNo = 0;
//        int pageSize = 0;
//        CustomerServiceImpl instance = new CustomerServiceImpl();
//        Pagination expResult = null;
//        Pagination result = instance.getPage(pageNo, pageSize);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testDeleteById() {
//        System.out.println("deleteById");
//        Integer id = null;
//        CustomerServiceImpl instance = new CustomerServiceImpl();
//        Customer expResult = null;
//        Customer result = instance.deleteById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testSave() {
//        System.out.println("save");
//        Customer bean = null;
//        CustomerServiceImpl instance = new CustomerServiceImpl();
//        Customer expResult = null;
//        Customer result = instance.save(bean);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testActivate() {
//        System.out.println("activate");
//        Customer bean = customerServiceImpl.findById(1);
//        customerServiceImpl.activate(bean);
//
////        CustomerServiceImpl instance = new CustomerServiceImpl();
////        Customer expResult = null;
////        Customer result = instance.activate(bean);
////        assertEquals(expResult, result);
////        fail("The test case is a prototype.");
//    }
//
    @Test
    public void testUpdateCredit() {
        System.out.println("updateCredit");
        Customer bean = customerServiceImpl.findById(1);
        Integer num = -5;
        customerServiceImpl.updateCredit(bean, num);
//        CustomerServiceImpl instance = new CustomerServiceImpl();
//        Customer expResult = null;
//        Customer result = instance.updateCredit(bean, num);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    }
}

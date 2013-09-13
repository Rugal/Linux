/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rugal.twodimensions.core.dao.CustomerDao;

/**
 *
 * @author rugal
 */
public class CustomerDaoImplTest {

    private static CustomerDao customerDaoImpl;

    public CustomerDaoImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        customerDaoImpl = (CustomerDao) ctx.getBean("customerDaoImpl");
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getPage method, of class CustomerDaoImpl.
     */
//    @Test
//    public void testGetPage() {
//        System.out.println("getPage");
//        int pageNo = 0;
//        int pageSize = 0;
//        CustomerDaoImpl instance = new CustomerDaoImpl();
//        Pagination expResult = null;
//        Pagination result = instance.getPage(pageNo, pageSize);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of findById method, of class CustomerDaoImpl.
//     */
//    @Test
//    public void testFindById() {
//        System.out.println("findById");
//        Integer id = null;
//        CustomerDaoImpl instance = new CustomerDaoImpl();
//        Customer expResult = null;
//        Customer result = instance.findById(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of save method, of class CustomerDaoImpl.
//     */
//    @Test
//    public void testSave() {
//        System.out.println("save");
//        Customer bean = new Customer();
//        bean.setName("Rugal Bernstein");
//        bean.setCredit(0);
//        bean.setMembership(0);
//        bean.setBirthday(new Date().getTime());
//        System.out.println(new Date().getTime());
//        customerDaoImpl.save(bean);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deleteById method, of class CustomerDaoImpl.
//     */
//    @Test
//    public void testDeleteById() {
//        customerDaoImpl.deleteById(2);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getEntityClass method, of class CustomerDaoImpl.
//     */
//    @Test
//    public void testGetEntityClass() {
//        System.out.println("getEntityClass");
//        CustomerDaoImpl instance = new CustomerDaoImpl();
//        Class expResult = null;
//        Class result = instance.getEntityClass();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}

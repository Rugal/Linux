/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.VendorDao;
import rugal.twodimensions.core.entity.Vendor;

/**
 *
 * @author rugal
 */
public class VendorDaoImplTest {
    
    private static VendorDao vendorDaoImpl;
    public VendorDaoImplTest() {
    }
    
    @Before
    public void setUp() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        vendorDaoImpl=(VendorDao) ctx.getBean("vendorDaoImpl");
    }
    
    @After
    public void tearDown() {
    }

//    @Test
//    public void testGetPage() {
//        System.out.println("getPage");
//        int pageNo = 0;
//        int pageSize = 0;
//        VendorDaoImpl instance = new VendorDaoImpl();
//        Pagination expResult = null;
//        Pagination result = instance.getPage(pageNo, pageSize);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testFindById() {
//        System.out.println("findById");
//        Integer id = null;
//        VendorDaoImpl instance = new VendorDaoImpl();
//        Vendor expResult = null;
//        Vendor result = instance.findById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
    @Test
    public void testSave() {
        System.out.println("save");
        Vendor bean = new Vendor();
        bean.setField("高达/初音");
        bean.setName("天神模型");
        bean.setWebsite("http://rugal.ml");
        vendorDaoImpl.save(bean);
//        VendorDaoImpl instance = new VendorDaoImpl();
//        Vendor expResult = null;
//        Vendor result = instance.save(bean);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    }
//
//    @Test
//    public void testDeleteById() {
//        System.out.println("deleteById");
//        Integer id = null;
//        VendorDaoImpl instance = new VendorDaoImpl();
//        Vendor expResult = null;
//        Vendor result = instance.deleteById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
}

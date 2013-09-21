/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao.impl;

import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.GoodsDao;
import rugal.twodimensions.core.dao.VendorDao;
import rugal.twodimensions.core.entity.Goods;
import rugal.twodimensions.core.entity.Vendor;

/**
 *
 * @author rugal
 */
public class GoodsDaoImplTest {

    private static GoodsDao goodsDaoImpl;
    private static VendorDao vendorDaoImpl;

    public GoodsDaoImplTest() {
    }

    @Before
    public void setUp() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        goodsDaoImpl = (GoodsDao) ctx.getBean("goodsDaoImpl");
        vendorDaoImpl = (VendorDao) ctx.getBean("vendorDaoImpl");
    }

    @After
    public void tearDown() {
    }
//    @Test
//    public void testGetPage() {
//        System.out.println("getPage");
//        int pageNo = 0;
//        int pageSize = 0;
//        GoodsDaoImpl instance = new GoodsDaoImpl();
//        Pagination expResult = null;
//        Pagination result = instance.getPage(pageNo, pageSize);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testFindByName() {
//        System.out.println("findByName");
//        String name = "";
//        GoodsDaoImpl instance = new GoodsDaoImpl();
//        List expResult = null;
//        List result = instance.findByName(name);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testFindById() {
//        System.out.println("findById");
//        Integer id = null;
//        GoodsDaoImpl instance = new GoodsDaoImpl();
//        Goods expResult = null;
//        Goods result = instance.findById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//

    @Test
    public void testSave() {
        System.out.println("save");
        Goods bean = new Goods();
        bean.setVid(vendorDaoImpl.findById(1));
        bean.setName("静音");
        bean.setQuantity(10);
        bean.setSellPrice(250);
        bean.setStockPrice(150);
        bean.setUnit("盒");

        goodsDaoImpl.save(bean);
//        GoodsDaoImpl instance = new GoodsDaoImpl();
//        Goods expResult = null;
//        Goods result = instance.save(bean);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    }
//
//    @Test
//    public void testDeleteById() {
//        System.out.println("deleteById");
//        Integer id = null;
//        GoodsDaoImpl instance = new GoodsDaoImpl();
//        Goods expResult = null;
//        Goods result = instance.deleteById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
}

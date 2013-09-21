/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao.impl;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.GoodsDao;
import rugal.twodimensions.core.dao.OperatorDao;
import rugal.twodimensions.core.dao.StockLogDao;
import rugal.twodimensions.core.dao.VendorDao;
import rugal.twodimensions.core.entity.StockLog;

/**
 *
 * @author rugal
 */
public class StockLogDaoImplTest {

    private static GoodsDao goodsDaoImpl;
    private static VendorDao vendorDaoImpl;
    private static OperatorDao operatorDaoImpl;
    private static StockLogDao stockLogDaoImpl;

    public StockLogDaoImplTest() {
    }

    @Before
    public void setUp() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        operatorDaoImpl = (OperatorDao) ctx.getBean("operatorDaoImpl");
        goodsDaoImpl = (GoodsDao) ctx.getBean("goodsDaoImpl");
        vendorDaoImpl = (VendorDao) ctx.getBean("vendorDaoImpl");
        stockLogDaoImpl = (StockLogDao) ctx.getBean("stockLogDaoImpl");
    }

    @After
    public void tearDown() {
    }

//    @Test
//    public void testGetPage() {
//        System.out.println("getPage");
//        int pageNo = 0;
//        int pageSize = 0;
//        StockLogDaoImpl instance = new StockLogDaoImpl();
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
//        StockLogDaoImpl instance = new StockLogDaoImpl();
//        StockLog expResult = null;
//        StockLog result = instance.findById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
    @Test
    public void testSave() {
        System.out.println("save");
        StockLog bean = new StockLog();
        bean.setOid(operatorDaoImpl.findById(1));
        bean.setVid(vendorDaoImpl.findById(1));
        bean.setGid(goodsDaoImpl.findById(4));
        bean.setQuantity(10);
        stockLogDaoImpl.save(bean);
//        StockLogDaoImpl instance = new StockLogDaoImpl();
//        StockLog expResult = null;
//        StockLog result = instance.save(bean);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    }
//
//    @Test
//    public void testDeleteById() {
//        System.out.println("deleteById");
//        Integer id = null;
//        StockLogDaoImpl instance = new StockLogDaoImpl();
//        StockLog expResult = null;
//        StockLog result = instance.deleteById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
}

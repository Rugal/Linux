/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao.impl;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.dao.CustomerDao;
import rugal.twodimensions.core.dao.GoodsDao;
import rugal.twodimensions.core.dao.OperatorDao;
import rugal.twodimensions.core.dao.VenditionLogDao;
import rugal.twodimensions.core.entity.Goods;
import rugal.twodimensions.core.entity.VenditionLog;

/**
 *
 * @author rugal
 */
public class VenditionLogDaoImplTest {

    private static OperatorDao operatorDaoImpl;
    private static GoodsDao goodsDaoImpl;
    private static CustomerDao customerDaoImpl;
    private static VenditionLogDao venditionLogDaoImpl;

    public VenditionLogDaoImplTest() {
    }

    @Before
    public void setUp() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        customerDaoImpl = (CustomerDao) ctx.getBean("customerDaoImpl");
        venditionLogDaoImpl = (VenditionLogDao) ctx.getBean("venditionLogDaoImpl");
        operatorDaoImpl = (OperatorDao) ctx.getBean("operatorDaoImpl");
        goodsDaoImpl = (GoodsDao) ctx.getBean("goodsDaoImpl");
    }

    @After
    public void tearDown() {
    }

//    @Test
//    public void testGetPage() {
//        System.out.println("getPage");
//        int pageNo = 0;
//        int pageSize = 0;
//        VenditionLogDaoImpl instance = new VenditionLogDaoImpl();
//        Pagination expResult = null;
//        Pagination result = instance.getPage(pageNo, pageSize);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
    @Test
    public void testGetPageByGoods() {
        System.out.println("getPageByGoods");
        boolean asc = true;
        int pageNo = 0;
        int pageSize = 10;
        Pagination result = venditionLogDaoImpl.getPageByGoods(asc, pageNo, pageSize);
        List<VenditionLog> list = (List<VenditionLog>) result.getList();
        for (int index = 0; index < list.size(); index++) {
            Object o = list.get(index);
            Object[] os = (Object[]) o;
            Goods g = (Goods) os[0];
            Long total = (Long) os[1];
            System.out.println(g.getName() + "----" + total);
        }
//        VenditionLogDaoImpl instance = new VenditionLogDaoImpl();
//        Pagination expResult = null;
//        Pagination result = instance.getPageByGoods(asc, pageNo, pageSize);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    }
//
//    @Test
//    public void testFindById() {
//        System.out.println("findById");
//        Integer id = null;
//        VenditionLogDaoImpl instance = new VenditionLogDaoImpl();
//        VenditionLog expResult = null;
//        VenditionLog result = instance.findById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testSave() {
//        System.out.println("save");
//        VenditionLog bean = new VenditionLog();
//        bean.setCid(customerDaoImpl.findById(1));
//        bean.setGid(goodsDaoImpl.findById(4));
//        bean.setOid(operatorDaoImpl.findById(1));
//        bean.setQuantity(1);
//        bean.setActualPrice(null);
//        venditionLogDaoImpl.save(bean);
////        VenditionLogDaoImpl instance = new VenditionLogDaoImpl();
////        VenditionLog expResult = null;
////        VenditionLog result = instance.save(bean);
////        assertEquals(expResult, result);
////        fail("The test case is a prototype.");
//    }
}

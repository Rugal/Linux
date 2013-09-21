/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service.impl;

import java.util.Iterator;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rugal.common.page.Pagination;
import rugal.twodimensions.core.entity.Goods;
import rugal.twodimensions.core.entity.VenditionLog;
import rugal.twodimensions.core.service.VenditionLogService;

/**
 *
 * @author rugal
 */
public class VenditionLogServiceImplTest {

    private static VenditionLogService venditionLogServiceImpl;

    public VenditionLogServiceImplTest() {
    }

    @Before
    public void setUp() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        venditionLogServiceImpl = (VenditionLogService) ctx.getBean("venditionLogServiceImpl");
    }

    @After
    public void tearDown() {
    }

//    @Test
//    public void testDeleteById() {
//        System.out.println("deleteById");
//        Integer id = null;
//        VenditionLogServiceImpl instance = new VenditionLogServiceImpl();
//        VenditionLog expResult = null;
//        VenditionLog result = instance.deleteById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
    public Object[] test(String... types) {
        return null;
    }

    @Test
    public void testGetPageByGoods() {
        System.out.println("getPageByGoods");
        boolean asc = false;
        int pageNo = 0;
        int pageSize = 10;
        Pagination result = venditionLogServiceImpl.getPageByGoods(asc, pageNo, pageSize);
        List list = result.getList();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Object object = it.next();
            Object[] os = (Object[]) object;
            Goods g = (Goods) os[0];
            Long total = (Long) os[1];
            System.out.println(g.getName() + " --- " + total);
        }
//        VenditionLogServiceImpl instance = new VenditionLogServiceImpl();
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
//        VenditionLogServiceImpl instance = new VenditionLogServiceImpl();
//        VenditionLog expResult = null;
//        VenditionLog result = instance.findById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testGetPage() {
//        System.out.println("getPage");
//        int pageNo = 0;
//        int pageSize = 0;
//        VenditionLogServiceImpl instance = new VenditionLogServiceImpl();
//        Pagination expResult = null;
//        Pagination result = instance.getPage(pageNo, pageSize);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testSave() {
//        System.out.println("save");
//        VenditionLog bean = null;
//        VenditionLogServiceImpl instance = new VenditionLogServiceImpl();
//        VenditionLog expResult = null;
//        VenditionLog result = instance.save(bean);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rugal.twodimensions.core.entity.Operator;
import rugal.twodimensions.core.service.OperatorService;

/**
 *
 * @author rugal
 */
public class OperatorServiceImplTest {

    private static OperatorService operatorServiceImpl;

    public OperatorServiceImplTest() {
    }

    @Before
    public void setUp() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        operatorServiceImpl = (OperatorService) ctx.getBean("operatorServiceImpl");
    }

    @After
    public void tearDown() {
    }

//    @Test
//    public void testDeleteById() {
//        System.out.println("deleteById");
//        Integer id = null;
//        OperatorServiceImpl instance = new OperatorServiceImpl();
//        Operator expResult = null;
//        Operator result = instance.deleteById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testFindById() {
//        System.out.println("findById");
//        Integer id = null;
//        OperatorServiceImpl instance = new OperatorServiceImpl();
//        Operator expResult = null;
//        Operator result = instance.findById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testGetPage() {
//        System.out.println("getPage");
//        int pageNo = 0;
//        int pageSize = 0;
//        OperatorServiceImpl instance = new OperatorServiceImpl();
//        Pagination expResult = null;
//        Pagination result = instance.getPage(pageNo, pageSize);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testSave() {
//        System.out.println("save");
//        Operator bean = null;
//        OperatorServiceImpl instance = new OperatorServiceImpl();
//        Operator expResult = null;
//        Operator result = instance.save(bean);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
   @Test
   public void testAuthenticate() {
       System.out.println("authenticate");
       Operator bean = operatorServiceImpl.findById(1);
       System.out.println(operatorServiceImpl.authenticate(bean));
////        OperatorServiceImpl instance = new OperatorServiceImpl();
////        boolean expResult = false;
////        boolean result = instance.authenticate(bean);
////        assertEquals(expResult, result);
////        fail("The test case is a prototype.");
   }
}

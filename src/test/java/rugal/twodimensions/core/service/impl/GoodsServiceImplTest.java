/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.service.impl;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rugal.twodimensions.core.entity.Customer;
import rugal.twodimensions.core.entity.Goods;
import rugal.twodimensions.core.entity.Operator;
import rugal.twodimensions.core.service.CustomerService;
import rugal.twodimensions.core.service.GoodsService;
import rugal.twodimensions.core.service.OperatorService;

/**
 *
 * @author rugal
 */
public class GoodsServiceImplTest {

    private static GoodsService goodsServiceImpl;
    private static OperatorService operatorServiceImpl;
    private static CustomerService customerServiceImpl;

    public GoodsServiceImplTest() {
    }

    @Before
    public void setUp() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        goodsServiceImpl = (GoodsService) ctx.getBean("goodsServiceImpl");
        operatorServiceImpl = (OperatorService) ctx.getBean("operatorServiceImpl");
        customerServiceImpl = (CustomerService) ctx.getBean("customerServiceImpl");
    }

    @After
    public void tearDown() {
    }

//    @Test
//    public void testDeleteById() {
//        System.out.println("deleteById");
//        Integer id = null;
//        GoodsServiceImpl instance = new GoodsServiceImpl();
//        Goods expResult = null;
//        Goods result = instance.deleteById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testFindById() {
//        System.out.println("findById");
//        Integer id = null;
//        GoodsServiceImpl instance = new GoodsServiceImpl();
//        Goods expResult = null;
//        Goods result = instance.findById(id);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testGetPage() {
//        System.out.println("getPage");
//        int pageNo = 0;
//        int pageSize = 0;
//        GoodsServiceImpl instance = new GoodsServiceImpl();
//        Pagination expResult = null;
//        Pagination result = instance.getPage(pageNo, pageSize);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
    @Test
    public void testFindByName() {
        System.out.println("findByName");
        String name = "初音";
        List<Goods> list = goodsServiceImpl.findByName(name);
        for (Goods goods : list) {
            System.out.println(goods.getName());
        }
//        GoodsServiceImpl instance = new GoodsServiceImpl();
//        List expResult = null;
//        List result = instance.findByName(name);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    }
//
//    @Test
//    public void testSell() {
//        System.out.println("sell");
//        Goods bean = goodsServiceImpl.findById(2);
//        Customer customer = customerServiceImpl.findById(1);
//        Operator operator = operatorServiceImpl.findById(2);
//        Integer quantity = 1;
//        Float price = 185.6f;
//        goodsServiceImpl.sell(bean, customer, operator, quantity, price);
////        GoodsServiceImpl instance = new GoodsServiceImpl();
////        Goods expResult = null;
////        Goods result = instance.sell(bean, customer, operator, quantity, price);
////        assertEquals(expResult, result);
////        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testStock() {
//        System.out.println("stock");
//        Goods bean = goodsServiceImpl.findById(1);
//        Operator operator = operatorServiceImpl.findById(2);
//        Integer num = -3;
//        goodsServiceImpl.stock(bean, operator, num);
//        GoodsServiceImpl instance = new GoodsServiceImpl();
//        Goods expResult = null;
//        Goods result = instance.stock(bean, operator, num);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testUpdateSellPrice() {
//        System.out.println("updateSellPrice");
//        Goods bean = null;
//        float price = 0.0F;
//        GoodsServiceImpl instance = new GoodsServiceImpl();
//        Goods expResult = null;
//        Goods result = instance.updateSellPrice(bean, price);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
}

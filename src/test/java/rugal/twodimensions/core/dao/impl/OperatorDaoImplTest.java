/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rugal.twodimensions.core.dao.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rugal.twodimensions.core.dao.OperatorDao;
import rugal.twodimensions.core.entity.Operator;

/**
 *
 * @author rugal
 */
public class OperatorDaoImplTest {
    
    private static OperatorDao operatorDaoImpl;
    public OperatorDaoImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        operatorDaoImpl=(OperatorDao) ctx.getBean("operatorDaoImpl");
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

    @Test
    public void testSomeMethod() {
        // TODO review the generated test code and remove the default call to fail.
        Operator bean=new Operator();
        bean.setName("苍蝇");
        bean.setPassword("900811");
        operatorDaoImpl.save(bean);
//        fail("The test case is a prototype.");
    }
}

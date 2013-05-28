package rugal.web.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

import rugal.food.dao.UserDao;
import rugal.food.entity.User;

public class UserDaoImplTest {
    private static UserDao userDao;
    
    @Before
        public void before() {
                ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
                userDao=(UserDao)ctx.getBean("userDaoImpl");
            }
    
    @Test
    public void testSave() {
        User u=new User();
        u.setUid("adel");
        userDao.save(u);
    }
}

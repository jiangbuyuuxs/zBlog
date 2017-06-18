package cn.mrz.dao;

import cn.mrz.mapper.UserMapper;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-test.xml"})
public class UserDaoTest extends TestCase {

    @Autowired
    ApplicationContext ctx;
    UserDao userDao = null;

    @Before
    public void setUp() {
        userDao = ctx.getBean("userDaoImpl",UserDao.class);
    }

    @Test
    public void testGetAllUser() throws Exception {
        userDao.getAllUser();
    }
}
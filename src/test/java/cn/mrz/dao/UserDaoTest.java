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

import java.util.List;
import java.util.Map;

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
        Map loggedUser = userDao.getLoggedUser();
        List<String> loggedInUserList = (List<String>)loggedUser.get("user");
        int unLoggedUserNum = (Integer)loggedUser.get("unLoggedNum");
    }
}
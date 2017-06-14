package cn.mrz.dao;

import cn.mrz.pojo.User;
import com.baomidou.mybatisplus.plugins.Page;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-test.xml"})
public class UserDaoTest extends TestCase {

    @Autowired
    ApplicationContext ctx;
    UserDao userDao = null;

    @Before
    public void setUp() {
        userDao = ctx.getBean("userDao", UserDao.class);
    }

    @Test
    public void testGetUserByUsername() throws Exception {
        User admin = userDao.getUserByUsername("admin");
        assertEquals("admin", admin.getUsername());
    }

    @Test
    public void testGetUsers() throws Exception {
        List<User> users = userDao.getUserList(new Page<User>(1, 10, "username"));
        for (User user : users) {
            System.out.println(user.getUsername());
        }
        //assertEquals("admin", users.get(0).getUsername());
        users = userDao.getUserList(new Page<User>(1, 10, "username"));
        for (User user : users) {
            System.out.println(user.getUsername());
        }
        users = userDao.getUserList(new Page<User>(1, 10));
        for (User user : users) {
            System.out.println(user.getUsername());
        }
    }
}
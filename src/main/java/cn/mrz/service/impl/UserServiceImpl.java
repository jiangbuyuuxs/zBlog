package cn.mrz.service.impl;

import cn.mrz.dao.UserDao;
import cn.mrz.mapper.UserMapper;
import cn.mrz.pojo.Permission;
import cn.mrz.pojo.Role;
import cn.mrz.pojo.User;
import cn.mrz.service.UserService;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/6.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserDao userDao;

    @Override
    public boolean addUser(User user) {
        User userByUsername = userMapper.getUserByUsername(user.getUsername());
        if (userByUsername == null) {
            Integer insert = userMapper.insert(user);
            return insert==1;
        } else {
            throw new RuntimeException("用户名已存在");
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public List<User> getUsers(int current, int pageSize, String sortField) {
        return userMapper.getUserList(new Page<User>(current, pageSize, sortField));
    }

    @Override
    public Set<Role> findRoles(String username) {
        return userMapper.findRoles(username);
    }

    @Override
    public Set<Permission> findPermissions(String username) {
        return userMapper.findPermissions(username);
    }


    @Override
    public void update(User user) {
        userMapper.updateById(user);
    }

    @Override
    public User getById(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public boolean delete(String id) {
        return userMapper.deleteById(id)==1;
    }

    @Override
    public List<String> getLoggedInUserList() {
        return userDao.getAllUser();
    }
}

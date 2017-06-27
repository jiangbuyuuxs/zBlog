package cn.mrz.service.impl;

import cn.mrz.dao.UserDao;
import cn.mrz.mapper.UserMapper;
import cn.mrz.pojo.Permission;
import cn.mrz.pojo.Role;
import cn.mrz.pojo.User;
import cn.mrz.service.UserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
            Long id = userMapper.insertUser(user);
            return id!=0;
        } else {
            throw new RuntimeException("用户名已存在");
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public Page<User> getUserList(Page<User> page) {
        page.setRecords(userMapper.getUserList(page));
        return page;
    }

    @Override
    public boolean updateNickname(String username, String nickname) {
        User userByUsername = userMapper.getUserByUsername(username);
        userByUsername.setNickname(nickname);
        Integer result = userMapper.updateById(userByUsername);
        return result==1;
    }

    @Override
    public int changeState(String username) {
        User userByUsername = userMapper.getUserByUsername(username);
        if("admin".equals(userByUsername.getUsername())){
            throw new RuntimeException("无法修改管理员状态");
        }
        int enabled = userByUsername.getEnabled();
        userByUsername.setEnabled(Math.abs(enabled-1));
        userMapper.updateById(userByUsername);
        return userByUsername.getEnabled();
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
    public Map getLoggedInUserList() {
        return userDao.getLoggedUser();
    }

}

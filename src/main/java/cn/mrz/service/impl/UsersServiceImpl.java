package cn.mrz.service.impl;

import cn.mrz.dao.UserDao;
import cn.mrz.pojo.Permission;
import cn.mrz.pojo.Role;
import cn.mrz.pojo.User;
import cn.mrz.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/6.
 */
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UserDao userDao;

    @Override
    public boolean addUser(User user) {
        User userByUsername = userDao.getUserByUsername(user.getUsername());
        if (userByUsername == null) {
            String password = user.getPassword();
           return false;
        } else {
            throw new RuntimeException("用户名已存在");
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public List<User> getUsers(int start, int num, String sortBy) {
        String sort = getPageInfo(sortBy);
        return userDao.getUsers(start, num, sort);
    }

    @Override
    public Set<Role> findRoles(String username) {
        return userDao.findRoles(username);
    }

    @Override
    public Set<Permission> findPermissions(String username) {
        return userDao.findPermissions(username);
    }

    private String getPageInfo(String sortBy){
        if(sortBy==null||sortBy.length()==0)
            return null;
        return null;
    }

    @Override
    public void update(User user) {
        userDao.updateById(user);
    }

    @Override
    public User getById(String id) {
        return userDao.selectById(id);
    }

    @Override
    public boolean delete(String id) {
        return userDao.deleteById(id)==1;
    }
}

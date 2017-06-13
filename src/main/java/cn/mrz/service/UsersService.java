package cn.mrz.service;

import cn.mrz.pojo.Permission;
import cn.mrz.pojo.Role;
import cn.mrz.pojo.User;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/12/1.
 */
public interface UsersService extends BaseService<User,String>{
    boolean addUser(User user);
    User getUserByUsername(String username);
    List<User> getUsers(int start, int num, String sortBy);

    Set<Role> findRoles(String username);

    Set<Permission> findPermissions(String username);
}

package cn.mrz.service;

import cn.mrz.pojo.Permission;
import cn.mrz.pojo.Role;
import cn.mrz.pojo.User;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/12/1.
 */
public interface UserService extends BaseService<User,String>{
    boolean addUser(User user);
    User getUserByUsername(String username);

    Set<Role> findRoles(String username);

    Set<Permission> findPermissions(String username);

    /**
     * 获取已登录的用户名和未登录用户数
     * @return
     */
    Map getLoggedInUserList();


    Page<User> getUserList(Page<User> page);

    boolean updateNickname(String username, String nickname);

    int changeState(String username);

}

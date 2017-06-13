package cn.mrz.dao;

import cn.mrz.pojo.Permission;
import cn.mrz.pojo.Role;
import cn.mrz.pojo.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/1.
 */
public interface UserDao extends BaseMapper<User> {
    User getUserByUsername(String username);
    List<User> getUsers(@Param("start")int start, @Param("num")int num, @Param("sort")String sort);

    Set<Role> findRoles(String username);

    Set<Permission> findPermissions(String username);
}

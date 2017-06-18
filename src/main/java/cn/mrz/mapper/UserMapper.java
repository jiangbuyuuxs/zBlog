package cn.mrz.mapper;

import cn.mrz.pojo.Permission;
import cn.mrz.pojo.Role;
import cn.mrz.pojo.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/1.
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    User getUserByUsername(String username);
    List<User> getUserList(Pagination page);

    Set<Role> findRoles(String username);

    Set<Permission> findPermissions(String username);
}

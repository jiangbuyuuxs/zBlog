package cn.mrz.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/6/16.
 */
public interface UserDao {
    List<String> getAllUser();
}

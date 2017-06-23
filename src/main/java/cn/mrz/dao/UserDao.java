package cn.mrz.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/16.
 */
public interface UserDao {
    /**
     * key user ArrayList<String>
     * key unLoggedNum Integer
     * @return
     */
    Map getLoggedUser();
}

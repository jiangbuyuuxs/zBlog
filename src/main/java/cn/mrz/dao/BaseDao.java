package cn.mrz.dao;

/**
 * Created by Administrator on 2017/8/3.
 */
public interface BaseDao {

    void setString(String key, String value, Long time);

    String getString(String key);
}

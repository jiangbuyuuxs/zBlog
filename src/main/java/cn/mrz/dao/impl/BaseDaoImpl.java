package cn.mrz.dao.impl;

import cn.mrz.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * Created by Administrator on 2017/6/16.
 */
public class BaseDaoImpl implements BaseDao {
    @Autowired
    protected RedisTemplate redisTemplate;

    @Override
    public void setString(String key,String value,Long time) {
        BoundValueOperations<String,String> boundValueOperations = redisTemplate.boundValueOps(key);
        boundValueOperations.set(value,time);
    }

    @Override
    public String getString(String key) {
        BoundValueOperations<String,String> boundValueOperations = redisTemplate.boundValueOps(key);
        return boundValueOperations.get();
    }
}

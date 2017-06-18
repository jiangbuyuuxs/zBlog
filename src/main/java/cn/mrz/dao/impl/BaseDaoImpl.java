package cn.mrz.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * Created by Administrator on 2017/6/16.
 */
public class BaseDaoImpl {
    @Autowired
    protected RedisTemplate redisTemplate;
}

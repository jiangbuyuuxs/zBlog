package cn.mrz.dao.impl;

import cn.mrz.dao.UserDao;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/6/16.
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

    @Override
    public List<String> getAllUser(){
        List<String> userList= new ArrayList<String>();
        Set<String> keys = redisTemplate.keys("spring:session:sessions:*");
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            if(key.indexOf("expires")!=-1)
                continue;
            BoundHashOperations<String,String,Object> boundHashOperations = redisTemplate.boundHashOps(key);
            if(boundHashOperations.hasKey("sessionAttr:username")) {
                Object userObject = boundHashOperations.get("sessionAttr:username");
                userList.add(userObject.toString());
            }
        }
        return userList;
    }
}

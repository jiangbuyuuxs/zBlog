package cn.mrz.dao.impl;

import cn.mrz.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Administrator on 2017/6/16.
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

    @Override
    public Map getLoggedUser(){
        Map result = new HashMap();
        List<String> userList = new ArrayList<String>();
        Set<String> expires = redisTemplate.keys("spring:session:sessions:expires*");
        Iterator<String> iterator = expires.iterator();
        while(iterator.hasNext()){
            String expiresKey = iterator.next();
            String[] expiresKeys = expiresKey.split(":");
            String sessionKeyId = expiresKeys[expiresKeys.length-1];
            BoundHashOperations<String,String,Object> boundHashOperations = redisTemplate.boundHashOps("spring:session:sessions:"+sessionKeyId);
            if(boundHashOperations.hasKey("sessionAttr:username")) {
                Object userObject = boundHashOperations.get("sessionAttr:username");
                userList.add(userObject.toString());
            }
        }
        result.put("user",userList);
        result.put("unLoggedNum",expires.size()-userList.size());
        return result;
    }

}

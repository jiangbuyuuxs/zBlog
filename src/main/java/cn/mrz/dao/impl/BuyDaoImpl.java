package cn.mrz.dao.impl;

import cn.mrz.dao.BuyDao;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */
@Repository
public class BuyDaoImpl extends BaseDaoImpl implements BuyDao {
    @Override
    public void addBuyFile(String key, String buyFile) {
        BoundListOperations<String,String> boundListOperations = redisTemplate.boundListOps(key);
        boundListOperations.leftPush(buyFile);
    }

    @Override
    public void removeBuyFile(String key, String buyFile) {
        BoundListOperations<String,String> boundListOperations = redisTemplate.boundListOps(key);
        boundListOperations.remove(1,buyFile);
    }

    @Override
    public List<String> listBuyFile(String key) {
        BoundListOperations<String,String> boundListOperations = redisTemplate.boundListOps(key);
        List<String> range = boundListOperations.range(0, -1);
        return range;
    }
}

package cn.mrz.dao.impl;

import cn.mrz.dao.ItemDao;
import cn.mrz.pojo.Item;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Administrator on 2017/6/19.
 */
@Repository
public class ItemDaoImpl extends BaseDaoImpl implements ItemDao {

    @Override
    public int setItemList(String key, List<Item> itemList) {
        BoundListOperations boundListOperations = redisTemplate.boundListOps(key);
        for(Item item:itemList){
            String itemJSON = JSONObject.toJSONString(item);
            boundListOperations.leftPush(itemJSON);
        }
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTimeInMillis(System.currentTimeMillis());
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        boundListOperations.expireAt(todayEnd.getTime());
        return boundListOperations.size().intValue();
    }

    @Override
    public List<Item> getItemList(String key) {
        List<Item> itemList = new ArrayList<Item>();
        BoundListOperations boundListOperations = redisTemplate.boundListOps(key);
        Long size = boundListOperations.size();
        while(size>0) {
            Object itemObject = boundListOperations.rightPop();
            String itemJSON = itemObject.toString();
            Item item = JSONObject.parseObject(itemJSON, Item.class);
            itemList.add(item);
            size--;
        }
        return itemList;
    }

    @Override
    public String getItemCount(String itemCountKey) {
        BoundValueOperations<String,String> boundValueOperations = redisTemplate.boundValueOps(itemCountKey);
        return boundValueOperations.get();
    }

    @Override
    public void setItemCount(String itemCountKey, int itemCount) {
        BoundValueOperations<String,String> boundValueOperations = redisTemplate.boundValueOps(itemCountKey);
        boundValueOperations.set(itemCount+"");
    }
}

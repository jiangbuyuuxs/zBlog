package cn.mrz.dao.impl;

import cn.mrz.dao.ItemDao;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/6/19.
 */
@Repository
public class ItemDaoImpl extends BaseDaoImpl implements ItemDao {
    @Override
    public void setItemClass(Integer itemClassHashCode, List<String> itemIdList) {
        SetOperations setOperations = redisTemplate.opsForSet();
        for(String itemId:itemIdList)
            setOperations.add("itemclass:" + itemClassHashCode, itemId);
    }

    @Override
    public void addItemClass(String itemType) {
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add("itemclass",itemType);

    }
    @Override
    public Set<String> getItemClassList() {
        SetOperations<String,String> setOperations = redisTemplate.opsForSet();
        return setOperations.members("itemclass");
    }

    @Override
    public List<String> getItemIdByClassHashcode(int itemClassHashCode) {
        SetOperations<String,String> setOperations = redisTemplate.opsForSet();
        Set<String> members = setOperations.members("itemclass:" + itemClassHashCode);
        List<String> list = new ArrayList<String>();
        list.addAll(members);
        return list;
    }
}

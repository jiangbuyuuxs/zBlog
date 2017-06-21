package cn.mrz.dao;

import cn.mrz.pojo.Item;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/6/19.
 */
public interface ItemDao {
    /**
     * 一级类目与具体商品
     * @param itemClassHashCode
     * @param itemIdList
     */
    void setItemClass(Integer itemClassHashCode, List<String> itemIdList);

    /**
     * 设置的一级类目添加到Set中
     * @param itemType
     */
    void addItemClass(String itemType);

    /**
     * 获取所有一级类目
     * @return
     */
    public Set<String> getItemClassList();

    List<String> getItemIdByClassHashcode(int itemClassHashCode);

    int setList(String key, List<Item> itemList);

    List<Item> getList(String key);
}

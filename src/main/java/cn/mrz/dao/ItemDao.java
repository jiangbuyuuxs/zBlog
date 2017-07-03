package cn.mrz.dao;

import cn.mrz.pojo.Item;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/6/19.
 */
public interface ItemDao {

    int setItemList(String key, List<Item> itemList);
    List<Item> getItemList(String key);

    String getItemCount(String itemCountKey);
    void setItemCount(String itemCountKey, int itemCount);
}

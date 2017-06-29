package cn.mrz.service;

import cn.mrz.pojo.Item;
import cn.mrz.pojo.ItemClass;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.File;
import java.util.Set;

/**
 * Created by Administrator on 2017/6/19.
 */
public interface BuyService {
    List<String> getBuyFileList();

    boolean saveBuyFile(MultipartFile buyFile);
    boolean deleteBuyFile(String buyFilePath);

    boolean parseBuyFile(String buyFilePath);
    Page<Item> getItem(Page<Item> page);

    Set<String> getItemClass();

    public List<ItemClass> getItemClassByParentId(Long parentId);

    List<String> getItemIdByClass(int itemClassHashCode);

    List<Item> getItemByItemIdList(List<String> itemIdList);

    List<Item> getCacheIndexItemList(String key);

    int cacheIndexItemList(String key,List<Item> itemList);

    String getCacheItemCount(String itemCountKey);

    void cacheItemCount(String itemCountKey, Integer itemCount);

    List<ItemClass> getSubItemClassByParentId(Long id);

    Page<Item> getItemByItemClass(Page<Item> page, String itemClass);
}

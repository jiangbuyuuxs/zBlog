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
    String fileDictionary = "C://attach//";
    String fileDictionary2 = "/home/attach";
    String ITEM_LIST_KEY_PREFIX = "item:list:";
    String ITEM_LIST_COUNT_KEY_PREFIX = "item:list:count:";

    List<String> getBuyFileList();
    boolean saveBuyFile(MultipartFile buyFile);
    boolean deleteBuyFile(String buyFilePath);
    boolean parseBuyFile(String buyFilePath) throws Exception;

    Page<Item> getItem(Page<Item> page);
    public List<ItemClass> getItemClassByParentId(Long parentId);

    List<ItemClass> getSubItemClassByParentId(Long id);
    Page<Item> getItemByItemClass(Page<Item> page, String itemClass);

    Page<Item> getItemList(Page<Item> itemPage, String itemClass);

    /**
     * 定时任务调用,清理过期的商品信息
     * @return
     */
    int clearObsoleteItem();

    List<String> listAnalysisFiles();

    void addHandlingBuyFile(String fileName);
}

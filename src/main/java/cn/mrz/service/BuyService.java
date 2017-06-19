package cn.mrz.service;

import cn.mrz.pojo.Item;
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

    boolean parseBuyFile(String buyFilePath);
    public Page<Item> getItem(Page<Item> page);

    public Set<String> getItemClass();

    List<String> getItemIdByClass(int itemClassHashCode);

    List<Item> getItemByItemIdList(List<String> itemIdList);

}

package cn.mrz.mapper;

import cn.mrz.pojo.Item;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/6/12.
 */
@Repository
public interface ItemMapper extends BaseMapper<Item>{
    int insertItem(Item item);
    List<Item> selectItem(Page page);

    List<Item> selectByItemClass(Page page,@Param("itemClass")String itemClass);

    Item selectByItemId(String itemId);

    int hasItemId(String itemId);

    int insertItemList(List<Item> itemBatch);
}

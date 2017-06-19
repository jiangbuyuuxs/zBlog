package cn.mrz.controller;

import cn.mrz.pojo.Item;
import cn.mrz.service.BuyService;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/6/19.
 */
@Controller
public class BuyController {

    @Autowired
    private BuyService buyService;

    @RequestMapping(value = "/buy")
    public String index(ModelMap map){
        Set<String> itemClass = buyService.getItemClass();
        Page<Item> pagination = buyService.getItem(new Page<Item>(0, 20));
        List<Item> itemList = pagination.getRecords();
        map.put("itemList",itemList);
        map.put("itemClassList",itemClass);
        return "/buy/index";
    }
    @RequestMapping(value = "/buy/{itemclass}")
    public String itemClass(@PathVariable("itemclass") int hashCode, ModelMap map){
        List<String> itemIdList= buyService.getItemIdByClass(hashCode);
        Set<String> itemClass = buyService.getItemClass();
        //TODO 这里的查询是全部查询,显示是不对的,先睡觉了
        List<Item> itemList = buyService.getItemByItemIdList(itemIdList);
        map.put("itemList",itemList);
        map.put("itemClassList",itemClass);
        return "/buy/index";
    }
}

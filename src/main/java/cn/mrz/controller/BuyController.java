package cn.mrz.controller;

import cn.mrz.pojo.Item;
import cn.mrz.service.BuyService;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by Administrator on 2017/6/19.
 */
@Controller
public class BuyController {

    @Autowired
    private BuyService buyService;

    @RequestMapping(value = "/buy")
    public String index(ModelMap map){

        Page<Item> pagination = buyService.getItem(new Page<Item>(0, 20));
        List<Item> itemList = pagination.getRecords();
        map.put("itemList",itemList);
        return "/buy/index";
    }
}

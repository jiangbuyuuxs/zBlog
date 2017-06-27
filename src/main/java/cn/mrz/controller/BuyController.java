package cn.mrz.controller;

import cn.mrz.exception.BuyFileExistException;
import cn.mrz.mapper.FavourableMapper;
import cn.mrz.mapper.ItemClassMapper;
import cn.mrz.mapper.ItemMapper;
import cn.mrz.pojo.Favourable;
import cn.mrz.pojo.Item;
import cn.mrz.pojo.ItemClass;
import cn.mrz.service.BuyService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2017/6/19.
 */
@Controller
public class BuyController {

    Logger logger = LoggerFactory.getLogger(BuyController.class);

    final static String INDEX_ITEM_KEY = "index:item";

    @Autowired
    private BuyService buyService;
    @Autowired
    private ItemClassMapper itemClassMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private FavourableMapper favourableMapper;

    @RequestMapping(value = "/buy/{page}")
    public String index(ModelMap map, @PathVariable int page) {
        List<ItemClass> itemClass = buyService.getItemClassByParentId(new Long(0));
        List<Item> cacheItemList = buyService.getCacheIndexItemList(INDEX_ITEM_KEY);
        List<Item> itemList = cacheItemList;
        if(cacheItemList==null||cacheItemList.size() == 0){
            Page<Item> pagination = new Page<Item>(page, 40, "sales_volume");
            pagination.setAsc(false);
            pagination = buyService.getItem(pagination);
            itemList = pagination.getRecords();
            //将首页数据存入redis,缓存一天
            buyService.cacheIndexItemList(INDEX_ITEM_KEY,itemList);
        }
        map.put("itemList", itemList);
        map.put("itemClassList", itemClass);
        return "/buy/index";
    }

    @RequestMapping(value = "/buy/{itemclass}/itemclass")
    public String itemClass(@PathVariable("itemclass") int hashCode, ModelMap map) {
        List<String> itemIdList = buyService.getItemIdByClass(hashCode);
        Set<String> itemClass = buyService.getItemClass();
        //这里直接在页面做一个假分页好了.反正数据量也不大
        List<Item> itemList = buyService.getItemByItemIdList(itemIdList);
        map.put("itemList", itemList);
        map.put("itemClassList", itemClass);
        return "/buy/index";
    }


    @RequestMapping(value = "/admin/buy/manager")
    public String itemClassManagerPage(ModelMap map) {
//        List<ItemClass> itemClassList = itemClassMapper.selectList( new EntityWrapper<ItemClass>());
        List<ItemClass> topItemClassList = itemClassMapper.selectTopClassList();
        List<ItemClass> subItemClassList = null;
        if (topItemClassList.size() > 0) {
            ItemClass itemClass = topItemClassList.get(0);
            subItemClassList = itemClassMapper.selectByParentId(itemClass.getId());
            map.put("defaultSelectedItem", itemClass);
        }
        //也可以用java来做,不过毕竟数据量小
        List<ItemClass> noRelationClassList = itemClassMapper.selectNoRelationClassList();
        map.put("noRelationClassList", noRelationClassList);
        map.put("topItemClassList", topItemClassList);
        map.put("subItemClassList", subItemClassList);
        return "/admin/buy/manager";
    }

    @RequestMapping(value = "/admin/buy/delete")
    public String deleteData(ModelMap map) {
        //TODO 删除
        if (false) {
            itemClassMapper.delete(new EntityWrapper<ItemClass>());
            itemMapper.delete(new EntityWrapper<Item>());
            favourableMapper.delete(new EntityWrapper<Favourable>());
            map.put("success", true);
            ItemClass itemClass1 = new ItemClass("衣不遮体了");
            itemClass1.setParentId(0);
            ItemClass itemClass2 = new ItemClass("食不果腹了");
            itemClass2.setParentId(0);
            ItemClass itemClass3 = new ItemClass("玩的不尽兴了");
            itemClass3.setParentId(0);
            ItemClass itemClass4 = new ItemClass("其他需求");
            itemClass4.setParentId(0);
            List<ItemClass> list = new ArrayList<ItemClass>();
            list.add(itemClass1);
            list.add(itemClass2);
            list.add(itemClass3);
            list.add(itemClass4);
            itemClassMapper.insertItemClassList2(list);
        }
        return "/admin/buy/manager";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/buy/update", produces = {"application/json;charset=UTF-8"})
    public String itemClassManagerMethod(@RequestParam Long id, @RequestParam Long selectedTopId, @RequestParam String method) {
        if ("set".equals(method)) {
            int result = itemClassMapper.updateParentIdById(selectedTopId, id);
            if (result == 1) {
                return "{\"success\":true}";
            } else {
                return "{\"success\":false,\"message\":\"添加子分类失败\"}";
            }
        } else if ("remove".equals(method)) {
            int result = itemClassMapper.removeParentIdById(id);
            if (result == 1) {
                return "{\"success\":true}";
            } else {
                return "{\"success\":false,\"message\":\"删除子分类失败\"}";
            }
        } else {
            return "{\"success\":false,\"message\":\"没有这样的方法\"}";

        }
    }

    @ResponseBody
    @RequestMapping(value = {"/admin/buy/get", "/buy/getsubitemclass"}, produces = {"application/json;charset=UTF-8"})
    public String itemClassManagerMethod(@RequestParam Long id) {
        EntityWrapper<ItemClass> entityWrapper = new EntityWrapper<ItemClass>();
        entityWrapper.where("parent_id={0}", id);
        List<ItemClass> subItemClassList = itemClassMapper.selectList(entityWrapper);
        Map map = new HashMap();
        map.put("success", true);
        map.put("subItemClassList", subItemClassList);

        return JSONObject.toJSONString(map);
    }


    @ResponseBody
    @RequestMapping(value = "/admin/buy/file/list", produces = {"application/json;charset=UTF-8"})
    public String getFileList() throws IOException {
        List<String> fileList = buyService.getBuyFileList();
        Map data = new HashMap();
        data.put("fileList", fileList);
        Map map = new HashMap();
        map.put("success", true);
        map.put("data", data);

        return JSONObject.toJSONString(map);
    }

    @ResponseBody
    @RequestMapping(value = "/admin/buy/file/upload", produces = {"application/json;charset=UTF-8"})
    public String uploadFile(@RequestParam MultipartFile buyFile) throws IOException {
        if(buyFile.isEmpty())
            return "{\"success\": false,\"message\":\"请选择非空文件\"}";
        String originalFilename = buyFile.getOriginalFilename();
        String[] originalFilenameSplit = originalFilename.split("\\.");
        if(originalFilenameSplit.length<2){
            return "{\"success\": false,\"message\":\"请选择正确的文件\"}";
        }else{
            String extName = originalFilenameSplit[originalFilenameSplit.length-1];
            if(!"xls".equals(extName)){
                return "{\"success\": false,\"message\":\"请选择正确的.xls文件\"}";
            }
        }
        try {
            boolean saveBuyFile = buyService.saveBuyFile(buyFile);
            String infoJson = "{\"success\": false}";
            if (saveBuyFile) {
                List<String> fileList = buyService.getBuyFileList();
                Map data = new HashMap();
                data.put("fileList", fileList);
                Map map = new HashMap();
                map.put("success", true);
                map.put("data", data);

                return JSONObject.toJSONString(map);
            } else {
                return infoJson;
            }
        }catch (BuyFileExistException e){
            return "{\"success\": false,\"message\":\"文件已存在\"}";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/admin/buy/file/parse", produces = {"application/json;charset=UTF-8"})
    public String parseFile(@RequestParam String fileName) throws IOException {
        if(buyService.parseBuyFile(fileName)){
            return "{\"success\": true,\"message\":\"成功解析\"}";
        }
        return "{\"success\": false,\"message\":\"解析失败\"}";

    }
    @ResponseBody
    @RequestMapping(value = "/admin/buy/file/delete", produces = {"application/json;charset=UTF-8"})
    public String deleteFile(@RequestParam String fileName) throws IOException {
        if(buyService.deleteBuyFile(fileName)){
            return "{\"success\": true,\"message\":\"成功删除:"+fileName+"\"}";
        }
        return "{\"success\": false,\"message\":\"删除失败\"}";

    }
}

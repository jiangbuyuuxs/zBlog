package cn.mrz.controller;

import cn.mrz.mapper.FavourableMapper;
import cn.mrz.mapper.ItemClassMapper;
import cn.mrz.mapper.ItemMapper;
import cn.mrz.mq.producer.MessageProducer;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2017/6/19.
 */
@Controller
public class BuyController extends BaseController {

    Logger logger = LoggerFactory.getLogger(BuyController.class);

    @Autowired
    private BuyService buyService;
    @Autowired
    private ItemClassMapper itemClassMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private FavourableMapper favourableMapper;

    @Autowired
    MessageProducer messageProducer;

    @RequestMapping(value = "/buy")
    public String index(ModelMap map) {
        int pageSize = 40;
        List<ItemClass> itemClass = buyService.getItemClassByParentId(0L);
        Integer itemCount;
        Page<Item> pagination = new Page<Item>(1, pageSize, "sales_volume");
        pagination.setAsc(false);
        pagination = buyService.getItem(pagination);
        List<Item> itemList = pagination.getRecords();
        itemCount = pagination.getTotal();
        double pageNum = Math.ceil(itemCount.floatValue() / pageSize);
        map.put("pageNum", pageNum);
        map.put("itemList", JSONObject.toJSONString(itemList));
        map.put("itemClassList", JSONObject.toJSONString(itemClass));
        return "/buy/index";
    }

    @RequestMapping(value = "/buy/{page}")
    public String index(ModelMap map, @PathVariable Integer page, @RequestParam(value = "itemclass", required = false) String itemClassStr, @RequestParam(value = "sort", required = false) String sortStr) {
        int pageSize = 40;
        String qStr = "";
        if ("".equals(itemClassStr)) {
            itemClassStr = null;
        }
        if (itemClassStr != null) {
            qStr = "?itemclass=" + itemClassStr;
        }
        String order = "sales_volume";
        boolean asc = false;//倒序排列
        if (sortStr != null && !"".equals(sortStr)) {
            String[] orderArr = sortStr.split("_");
            order = orderArr[0];
            String ascStr = orderArr[1];//顺序
            if ("0".equals(ascStr)) {
                asc = false;
            }else if ("1".equals(ascStr)) {
                asc = true;
            }else{
                throw new RuntimeException("排序参数格式不对,不要乱搞~~~");
            }
        }

        List<ItemClass> topItemClassList = buyService.getItemClassByParentId(0L);
        List subItemClassList = new ArrayList();
        boolean getItemClass = false;
        for (ItemClass topItemClass : topItemClassList) {
            List<ItemClass> subItemClass = buyService.getItemClassByParentId(topItemClass.getId());
            subItemClassList.add(subItemClass);
            if (itemClassStr != null && !getItemClass) {
                for (ItemClass itemClass : subItemClass) {
                    if (itemClassStr.equals(itemClass.getHashCode() + "")) {
                        map.put("topItemClass", JSONObject.toJSONString(topItemClass));
                        map.put("subItemClass", JSONObject.toJSONString(itemClass));
                        getItemClass = true;
                        break;
                    }
                }
            }
        }

        Page<Item> pagination = new Page<Item>(page, pageSize, order);
        pagination.setAsc(asc);
        pagination = buyService.getItemList(pagination, itemClassStr);
        List<Item> itemList = pagination.getRecords();
        Integer itemCount = pagination.getTotal();
        double pageNum = Math.ceil(itemCount.floatValue() / pageSize);

        if (!getItemClass) {
            map.put("topItemClass", "null");
            map.put("subItemClass", "null");
        }

        map.put("qStr", qStr);
        map.put("pageNum", pageNum);
        map.put("curPage", page);
        map.put("itemList", JSONObject.toJSONString(itemList));
        map.put("topItemClassList", JSONObject.toJSONString(topItemClassList));
        map.put("subItemClassList", JSONObject.toJSONString(subItemClassList));
        return "/buy/index2";
    }

    @ResponseBody
    @RequestMapping(value = "/buy/itemlist", produces = {"application/json;charset=UTF-8"})
    public String getItemList(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "itemClass", required = false) String itemClass, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageSize == null)
            pageSize = 40;
        if (page == null)
            page = 1;
        Page<Item> pagination = new Page<Item>(page, 40, "sales_volume");
        pagination.setAsc(false);
        if (itemClass != null && !"".equals(itemClass)) {
            pagination = buyService.getItemByItemClass(pagination, itemClass);
        } else {
            pagination = buyService.getItem(pagination);
        }
        List<Item> itemList = pagination.getRecords();
        Integer itemCount = pagination.getTotal();
        Map data = new HashMap();
        double pageNum = Math.ceil(itemCount.floatValue() / pageSize);
        data.put("pageNum", pageNum);
        data.put("itemList", itemList);
        Map map = new HashMap();
        map.put("success", true);
        map.put("data", data);
        return JSONObject.toJSONString(map);

    }

    @ResponseBody
    @RequestMapping(value = "/buy/subitemclass", produces = {"application/json;charset=UTF-8"})
    public String itemClass(@RequestParam(value = "id", required = false) Long id) {
        List<ItemClass> subItemClassList = buyService.getSubItemClassByParentId(id);
        Map data = new HashMap();
        data.put("subItemClassList", subItemClassList);
        Map map = new HashMap();
        map.put("success", true);
        map.put("data", data);
        return JSONObject.toJSONString(map);

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
    @RequestMapping(value = "/admin/buy/files", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public String getFileList() throws IOException {
        List<String> fileList = buyService.getBuyFileList();
        List<String> analysisFiles = buyService.listAnalysisFiles();
        Map data = new HashMap();
        data.put("fileList", fileList);
        data.put("analysisFiles", analysisFiles);
        Map map = new HashMap();
        map.put("success", true);
        map.put("data", data);

        return JSONObject.toJSONString(map);
    }

    @ResponseBody
    @RequestMapping(value = "/admin/buy/files", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public String uploadFile(@RequestParam MultipartFile buyFile) throws IOException {
        //TODO 这个东西是先将文件上传,然后才有这些操作.所以说判断什么的不能在这里做,先于这里才可以
        if (buyFile.isEmpty())
            return "{\"success\": false,\"message\":\"请选择非空文件\"}";
        String originalFilename = buyFile.getOriginalFilename();
        String[] originalFilenameSplit = originalFilename.split("\\.");
        if (originalFilenameSplit.length < 2) {
            return "{\"success\": false,\"message\":\"请选择正确的文件\"}";
        } else {
            String extName = originalFilenameSplit[originalFilenameSplit.length - 1];
            if (!"xls".equals(extName)) {
                return "{\"success\": false,\"message\":\"请选择正确的.xls文件\"}";
            }
        }
        boolean saveBuyFile = buyService.saveBuyFile(buyFile);
        if (saveBuyFile) {
            List<String> fileList = buyService.getBuyFileList();
            List<String> handlingBuyFileList = buyService.listAnalysisFiles();
            Map data = new HashMap();
            data.put("fileList", fileList);
            data.put("handlingFileList", handlingBuyFileList);
            Map map = new HashMap();
            map.put("success", true);
            map.put("data", data);

            return JSONObject.toJSONString(map);
        } else {
            return DEFAULT_FAILED_MESSAGE;
        }
    }
    @ResponseBody
    @RequestMapping(value = "/admin/buy/files",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
    public String deleteFile(@RequestParam String fileName) throws IOException {
        if (buyService.deleteBuyFile(fileName)) {
            return "{\"success\": true,\"message\":\"成功删除:" + fileName + "\"}";
        }
        return "{\"success\": false,\"message\":\"删除失败\"}";
    }

    /**
     * 开始解析给定文件
     * @param fileName
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/admin/buy/files/analysis",method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public String analysisFile(@RequestParam String fileName) throws IOException {
        if(fileName!=null){
            messageProducer.sendHandlerTbkExcelMessage(fileName);
            buyService.addHandlingBuyFile(fileName);
        }
        Map data = new HashMap();
        List<String> analysisFiles = buyService.listAnalysisFiles();
        data.put("analysisFiles", analysisFiles);
        Map map = new HashMap();
        map.put("success", true);
        map.put("data", data);
        return JSONObject.toJSONString(map);
    }

    /**
     * 获取正在解析中的文件列表
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/admin/buy/files/analysis",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    public String analysisFiles() throws IOException {
        Map data = new HashMap();
        List<String> analysisFiles = buyService.listAnalysisFiles();
        data.put("analysisFiles", analysisFiles);
        Map map = new HashMap();
        map.put("success", true);
        map.put("data", data);
        return JSONObject.toJSONString(map);
    }

    @ResponseBody
    @RequestMapping(value = "/admin/buy/obsoleteItem",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
    public String deleteObsoleteItem() throws IOException {
        int clearObsoleteItem = buyService.clearObsoleteItem();
        return "{\"success\": true,\"message\":\"成功清除过期商品:" + clearObsoleteItem + "条\"}";
    }

}

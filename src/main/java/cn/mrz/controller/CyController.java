package cn.mrz.controller;

import cn.mrz.mapper.CyMapper;
import cn.mrz.pojo.Cy;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/6.
 */
@Controller
public class CyController  extends BaseController{

    @Resource
    private CyMapper cyDao;

    @RequestMapping(value = {"/cy/cyjl"})
    public String goCyPage() {
        return "/cy/cy";
    }

    @ResponseBody
    @RequestMapping(value = {"/cy/cy/cy"}, produces = {"application/json;charset=UTF-8"})
    public String getCy(@RequestParam(required = false) String cy) {
        if(cy==null){
            cy = "自力更生";
        }
        //TODO 这里是可以按照参数输入来减少一步sql查询的.
        String json = "{\"success\":false}";
        if (cy != null && cy.trim().length() > 2) {
            Cy chenyu = cyDao.getCy(cy);
            if (chenyu != null) {
                String pyEnd = chenyu.getPyEnd();
                List<Cy> cyByPyFirst = cyDao.getCyByPyFirst(pyEnd);
                Map<String, Object> map = new HashMap();
                map.put("success", true);
                map.put("cy", chenyu);
                map.put("num", 0);
                if (cyByPyFirst != null && cyByPyFirst.size() > 0) {
                    map.put("num", cyByPyFirst.size());
                    map.put("nextcy", cyByPyFirst);
                }
                json = JSONObject.toJSONString(map);
            }
        }
        return json;
    }

}

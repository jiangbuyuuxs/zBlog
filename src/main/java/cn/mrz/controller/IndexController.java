package cn.mrz.controller;

import cn.mrz.exception.NoSuchBlogException;
import cn.mrz.pojo.Blog;
import cn.mrz.pojo.Word;
import cn.mrz.service.BlogService;
import cn.mrz.service.WordService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class IndexController {

    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private BlogService blogService;

    @Resource
    private WordService wordService;

    @RequestMapping(value = {"/", "index", "home"})
    public String goIndex(ModelMap map) {
        return "/index";
    }

    @RequestMapping(value = {"/go/{pageName}"})
    public String goPage(@PathVariable String pageName) {
        return "/" + pageName;
    }

    @ResponseBody
    @RequestMapping(value = {"/visitblog/{blogId}"}, produces = "application/javascript")
    public String visitBlog(@PathVariable int blogId) {
        blogService.addVisit(blogId);
        return "console.log(\"visit success\");";
    }
}

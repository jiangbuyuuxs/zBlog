package cn.mrz.controller;

import cn.mrz.exception.NoSuchBlogException;
import cn.mrz.pojo.Blog;
import cn.mrz.pojo.Word;
import cn.mrz.service.BlogService;
import cn.mrz.service.WordService;
import com.baomidou.mybatisplus.plugins.Page;
import org.codehaus.jackson.map.ObjectMapper;
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

    private final int pageSize = 15;

    @Resource
    private BlogService blogService;

    @Resource
    private WordService wordService;

    @RequestMapping(value = {"/", "index", "home"})
    public String goIndex(ModelMap map) {
        Page<Blog> pagination = new Page<Blog>(1, pageSize, "create_date");
        pagination = blogService.getBlogList(pagination, false, false);
        List<Blog> blogList = pagination.getRecords();
        int blogCountNum = pagination.getTotal();//blogService.getBlogCountNum();
        List<Blog> hotBlogList = blogService.getHotBlogList(5);
        List<Word> hotWordList = wordService.getTopHotWordList(15);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd mm:HH:ss"));
        String blogListJson;
        String hotBlogListJson;
        String hotWordListJson;
        try {
            blogListJson = mapper.writeValueAsString(blogList);
        } catch (IOException e) {
            blogListJson = "{\"success\": false,\"msg\",\"获取信息失败\"}";
        }
        try {
            hotBlogListJson = mapper.writeValueAsString(hotBlogList);
        } catch (IOException e) {
            hotBlogListJson = "{\"success\": false,\"msg\",\"获取热门博文失败\"}";
        }
        try {
            hotWordListJson = mapper.writeValueAsString(hotWordList);
        } catch (IOException e) {
            hotWordListJson = "{\"success\": false,\"msg\",\"获取热词失败\"}";
        }
        map.addAttribute("blogList", blogListJson);
        map.addAttribute("blogCountNum", blogCountNum);
        map.addAttribute("hotBlogList", hotBlogListJson);
        map.addAttribute("hotWordList", hotWordListJson);
        map.addAttribute("pageSize", pageSize);
        return "/index";
    }

    @RequestMapping(value = {"/go/{pageName}"})
    public String goPage(@PathVariable String pageName) {
        return "/" + pageName;
    }

    @RequestMapping(value = "/detail/{id}/id")
    public String goBlogDetailPage(@PathVariable Long id, ModelMap map) {
        Blog blog = blogService.getById(id);
        if (blog == null) {
            throw new NoSuchBlogException();
        }
        map.addAttribute("blog", blog);
        return "/detail";
    }

    @RequestMapping(value = {"/hotword/{hashcode}/id"})
    public String goHotWordList(ModelMap map,@PathVariable String hashcode) {
        int pageSize = 10;
        Page<Word> pagination = wordService.getWordsByWordHash(new Page<Word>(1,pageSize), hashcode);
        List<Word> records = pagination.getRecords();
        int blogCountNum = pagination.getTotal();
        List<Blog> blogList = blogService.getBlogListByWordList(records);
        String hotWord = "";
        for(Word word : records){
            if("".equals(hotWord)) {
                hotWord = word.getRemark();
                break;
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd mm:HH:ss"));
        String blogListJson = "";
        try {
            blogListJson = mapper.writeValueAsString(blogList);
        } catch (IOException e) {
            logger.info("获取热词对应的博客列表时,转换失败 :"+e.getLocalizedMessage());
            return "[]";

        }
        map.addAttribute("blogList",blogListJson);
        map.addAttribute("blogCountNum",blogCountNum);
        map.addAttribute("pageSize",pageSize);
        map.addAttribute("hashcode",hashcode);
        map.addAttribute("hotWord",hotWord);
        return "/hotword";
    }

    @ResponseBody
    @RequestMapping(value = {"/hotword/{hashcode}/id/{page}/page/{pageSize}/pagesize"}, produces = {"application/json;charset=UTF-8"})
    public String getHotWordList(@PathVariable String hashcode,@PathVariable Integer page,@PathVariable Integer pageSize) {

        Page<Word> pagination = wordService.getWordsByWordHash(new Page<Word>(page, pageSize), hashcode);
        List<Word> records = pagination.getRecords();
        List<Blog> blogList = blogService.getBlogListByWordList(records);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd mm:HH:ss"));
        HashMap hashMap = new HashMap();
        hashMap.put("blogList", blogList);
        String blogListJson = "";
        try {
            blogListJson = mapper.writeValueAsString(hashMap);
        } catch (IOException e) {
            logger.info("获取热词对应的博客列表时,转换失败 :"+e.getLocalizedMessage());
            return "[]";
        }
        return blogListJson;
    }

    @ResponseBody
    @RequestMapping(value = {"/visitblog/{blogId}"}, produces = "application/javascript")
    public String visitBlog(@PathVariable int blogId) {
        blogService.addVisit(blogId);
        return "console.log(\"visit success\");";
    }
}

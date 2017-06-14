package cn.mrz.controller;

import cn.mrz.pojo.Blog;
import cn.mrz.pojo.Word;
import cn.mrz.service.BlogService;
import cn.mrz.service.WordService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    private final int pageSize = 15;

    @Resource
    private BlogService blogService;

    @Resource
    private WordService wordService;

    @RequestMapping(value = {"/", "index", "home"})
    public String goIndex(ModelMap map) {
        List<Blog> blogList = blogService.getBlogList(1, pageSize, "create_date", false, false);
        int blogCountNum = blogService.getBlogCountNum();
        List<Blog> hotBlogList = blogService.getHotBlogList(5);
        List<Word> hotWordList = wordService.getTopHotWordList(15);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd mm:HH:ss"));
        String blogListJson;
        String hotBlogListJson;
        String hotWordListJson;
        try {
            blogListJson = mapper.writeValueAsString(blogList);
            hotBlogListJson = mapper.writeValueAsString(hotBlogList);
            hotWordListJson = mapper.writeValueAsString(hotWordList);
        } catch (IOException e) {
            blogListJson = "{\"success\": false,\"msg\",\"获取信息失败\"}";
            hotBlogListJson = "{\"success\": false,\"msg\",\"获取热门博文失败\"}";
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

    @ResponseBody
    @RequestMapping(value = "/blog/{page}/page/{order}/order", produces = {"application/json;charset=UTF-8"})
    public String getBlogListJson(@PathVariable int page, @PathVariable String order) {
        List<Blog> blogList = blogService.getBlogList(page, pageSize, null, null, false);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd mm:HH:ss"));
        String blogData = "";
        try {
            blogData = mapper.writeValueAsString(blogList);
        } catch (IOException e) {
            return "{\"success\": false,\"msg\",\"转换失败\"}";
        }
        return "{\"success\": true,\"data\":" + blogData + "}";
    }

    @RequestMapping(value = "/detail/{id}/id")
    public String goDetail(@PathVariable Long id, ModelMap map) {
        Blog blog = blogService.getById(id);

        if (blog == null)
            return "redirect:/go/error";
        map.addAttribute("blog", blog);
        return "/detail";
    }

    @RequestMapping(value = {"/hotword/{hashcode}/id"})
    public String getHotWordList(ModelMap map,@PathVariable String hashcode) {
        List<Word> wordsByWordHash = wordService.getWordsByWordHash(hashcode);
        List<Blog> blogList = new ArrayList<Blog>();
        for(Word word : wordsByWordHash){
            blogList.add(blogService.getById(word.getBlogId()));
        }
        map.addAttribute("blogList",blogList);
        return "/hotword";
    }

    @ResponseBody
    @RequestMapping(value = {"/visitblog/{blogId}"}, produces = "application/javascript")
    public String visitBlog(@PathVariable int blogId) {
        blogService.addVisit(blogId);
        return "console.log(\"visit success\");";
    }
}

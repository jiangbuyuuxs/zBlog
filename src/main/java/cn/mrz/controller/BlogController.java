package cn.mrz.controller;

import cn.mrz.exception.NoSuchBlogException;
import cn.mrz.exception.NoSuchWordException;
import cn.mrz.pojo.Blog;
import cn.mrz.pojo.Word;
import cn.mrz.service.BlogService;
import cn.mrz.service.WordService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/6.
 */
@Controller
public class BlogController {

    final int pageSize = 10;

    @Resource
    private BlogService blogService;
    @Resource
    private WordService wordService;

    @ResponseBody
    @RequestMapping(value = {"/admin/userblog/{username}/username"}, produces = {"application/json;charset=UTF-8"})
    public String getUserBlogList(@PathVariable String username) {
        List<Blog> userBlogList = blogService.getUserBlogList(username);
        HashMap hashMap = new HashMap();
        hashMap.put("success", true);
        hashMap.put("userBlogList", userBlogList);
        return JSONObject.toJSONString(hashMap);
    }

    @RequestMapping(value = {"/admin/blog/go/add"})
    public String goNewBlog(ModelMap map) {
        map.addAttribute("oper", "新增博文");
        return "/admin/blog/edit";
    }

    @RequestMapping(value = {"/admin/blog/{id}/edit"})
    public String goEditBlog(ModelMap map, @PathVariable Long id) {
        Blog blog = blogService.getById(id);
        map.addAttribute("oper", "修改博文");
        map.addAttribute("blog", blog);
        return "/admin/blog/edit";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/blog/add")
    public String addBlog(Blog blog) {
        Date now = new Date(System.currentTimeMillis());
        blog.setCreateDate(now);
        blog.setEditDate(now);
        if (blog.getTitle() == null || "".equals(blog.getTitle().trim()))
            blog.setTitle(new SimpleDateFormat("yyyy年MM月dd日HH时m分ss秒").format(now) + "写下的博客");
        if(blog.getAuthor()==null){
            blog.setAuthor(SecurityUtils.getSubject().getPrincipal().toString());
        }
        blogService.addBlog(blog);
        final Blog blog2 = blog;
        new Thread(new Runnable() {
            @Override
            public void run() {
                wordService.getBlogWords(blog2);
            }
        }
        ).start();
        return "{\"success\": true}";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/blog/edit")
    public String editBlog(Blog blog) {
        if (null == blog) {
            return "{\"success\": false}";
        }
        Blog oldBlog = blogService.getById(blog.getId());
        if (null == oldBlog) {
            return "{\"success\": false}";
        }
        blog.setEditDate(new Date(System.currentTimeMillis()));
        blogService.update(blog);
        final Blog blog2 = blog;
        new Thread(new Runnable() {
            @Override
            public void run() {
                wordService.getBlogWords(blog2);
            }
        }).start();
        return "{\"success\": true}";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/blog/{id}/del/{page}/page", produces = {"application/json;charset=UTF-8"})
    public String delBlog(@PathVariable("id")Long id,@PathVariable("page") Integer page) {
        String blogListJson = "{\"success\": false,\"msg\",\"操作失败\"}";
        boolean isDelete = blogService.deleteBlogById(id);
        if(isDelete){
            Page<Blog> pagination = new Page<Blog>(page,pageSize,"create_date");
            pagination= blogService.getBlogList(pagination, false, false);
            List<Blog> blogList = pagination.getRecords();
            int blogCountNum = pagination.getTotal();//blogService.getBlogCountNum();
            HashMap hashMap = new HashMap();
            hashMap.put("blogList", blogList);
            hashMap.put("blogCountNum", blogCountNum);
                blogListJson = JSONObject.toJSONString(hashMap);
        }
        return blogListJson;
    }


    @ResponseBody
    @RequestMapping(value = "/blog/list", produces = {"application/json;charset=UTF-8"})
    public String getBlogList(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer pageSize,@RequestParam(required = false) String sort) {
        if(page==null)
            page = 1;
        if(pageSize==null)
            pageSize = this.pageSize;
        if(sort==null)
            sort = "create_date";
        Page<Blog> pagination = new Page<Blog>(page, pageSize,sort);
        pagination = blogService.getBlogList(pagination, false, false);
        List<Blog> blogList = pagination.getRecords();
        Integer blogCountNum = pagination.getTotal();
        Map map = new HashMap();
        map.put("success",true);
        Map data = new HashMap();
        data.put("blogList",blogList);
        double pageNum = Math.ceil(blogCountNum.doubleValue() / pageSize.doubleValue());
        data.put("pageNum",pageNum);
        map.put("data",data);
        return JSONObject.toJSONString(map);
    }

    @ResponseBody
    @RequestMapping(value = "/blog/hotword", produces = {"application/json;charset=UTF-8"})
    public String getHotWordList() {
        List<Word> hotWordList = wordService.getTopHotWordList(15);
        Map map = new HashMap();
        map.put("success",true);
        Map data = new HashMap();
        data.put("hotWordList",hotWordList);
        map.put("data",data);
        return JSONObject.toJSONString(map);
    }

    @ResponseBody
    @RequestMapping(value = "/blog/topblog", produces = {"application/json;charset=UTF-8"})
    public String getTopBlogList() {
        List<Blog> topBlogList = blogService.getHotBlogList(5);
        Map map = new HashMap();
        map.put("success",true);
        Map data = new HashMap();
        data.put("topBlogList",topBlogList);
        map.put("data",data);
        return JSONObject.toJSONString(map);
    }

    @RequestMapping(value = "/blog/blog/{id}")
    public String goBlogPage(@PathVariable Long id, ModelMap map) {
        Blog blog = blogService.getById(id);
        if (blog == null) {
            throw new NoSuchBlogException();
        }
        map.addAttribute("blog", blog);
        return "/blog/blog";
    }

    @RequestMapping(value = {"/blog/hotword/{hashcode}"})
    public String goHotWordList(ModelMap map,@PathVariable String hashcode) {
        String hotWord = "";
        Page<Word> wordsByWordHash = wordService.getWordsByWordHash(new Page<Word>(1,1), hashcode);
        if(wordsByWordHash.getTotal()>0){
            Word word = wordsByWordHash.getRecords().get(0);
            hotWord = word.getRemark();
        }else{
            throw new NoSuchWordException();
        }
        map.addAttribute("hotWord",hotWord);
        map.addAttribute("hashcode",hashcode);
        return "/blog/hotword";
    }

    @ResponseBody
    @RequestMapping(value = {"/blog/hotword/list/{hashcode}"}, produces = {"application/json;charset=UTF-8"})
    public String getHotWordList(@PathVariable String hashcode,@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer pageSize) {
        if(page==null)
            page = 1;
        if(pageSize==null)
            pageSize = 10;
        Page<Word> pagination = wordService.getWordsByWordHash(new Page<Word>(page, pageSize), hashcode);
        List<Word> records = pagination.getRecords();
        Integer total = pagination.getTotal();
        List<Blog> blogList = blogService.getBlogListByWordList(records);

        HashMap hashMap = new HashMap();
        hashMap.put("success", true);
        HashMap data = new HashMap();
        double pageNum = Math.ceil(total.doubleValue() / pageSize.doubleValue());
        data.put("blogList", blogList);
        data.put("pageNum", pageNum);
        hashMap.put("data",data);
        String blogListJson = JSONObject.toJSONString(hashMap);
        return blogListJson;
    }

    @Deprecated
    @ResponseBody
    @RequestMapping(value = {"/admin/blog/{page}/page/{pageSize}/pagesize"}, produces = {"application/json;charset=UTF-8"})
    public String getBlogList(@PathVariable Integer page,@PathVariable Integer pageSize) {
        if(pageSize==null)
            pageSize = this.pageSize;
        Page<Blog> pagination = new Page<Blog>(page,pageSize,"create_date");
        pagination = blogService.getBlogList(pagination, false, false);
        List<Blog> blogList = pagination.getRecords();
        int blogCountNum = pagination.getTotal();//blogService.getBlogCountNum();
        HashMap hashMap = new HashMap();
        hashMap.put("success", true);
        hashMap.put("blogList", blogList);
        hashMap.put("blogCountNum", blogCountNum);
        return JSONObject.toJSONString(hashMap);
    }

}

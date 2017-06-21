package cn.mrz.controller;

import cn.mrz.pojo.Blog;
import cn.mrz.service.BlogService;
import cn.mrz.service.WordService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    @RequestMapping(value = {"/admin/blog/{page}/page/{pageSize}/pagesize"}, produces = {"application/json;charset=UTF-8"})
    public String getBlogList(@PathVariable Integer page,@PathVariable Integer pageSize) {
        if(pageSize==null)
            pageSize = this.pageSize;
        Page<Blog> pagination = new Page<Blog>(page,pageSize,"create_date");
        pagination = blogService.getBlogList(pagination, false, false);
        List<Blog> blogList = pagination.getRecords();
        int blogCountNum = pagination.getTotal();//blogService.getBlogCountNum();
        HashMap hashMap = new HashMap();
        hashMap.put("blogList", blogList);
        hashMap.put("blogCountNum", blogCountNum);

        return JSONObject.toJSONString(hashMap);

    }

    @RequiresRoles("admin")
    @RequestMapping(value = {"/admin/blog/go/add"})
    public String goNewBlog(ModelMap map) {
        map.addAttribute("oper", "新增博文");
        return "/admin/blog/edit";
    }

    @RequiresRoles("admin")
    @RequestMapping(value = {"/admin/blog/{id}/edit"})
    public String goEditBlog(ModelMap map, @PathVariable Long id) {
        Blog blog = blogService.getById(id);
        map.addAttribute("oper", "修改博文");
        map.addAttribute("blog", blog);
        return "/admin/blog/edit";
    }

    @RequiresRoles("admin")
    @ResponseBody
    @RequestMapping(value = "/admin/blog/add")
    public String addBlog(Blog blog) {
        Date now = new Date(System.currentTimeMillis());
        blog.setCreateDate(now);
        blog.setEditDate(now);
        if (blog.getTitle() == null || "".equals(blog.getTitle().trim()))
            blog.setTitle(new SimpleDateFormat("yyyy年MM月dd日HH时m分ss秒").format(now) + "写下的博客");
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

    @RequiresRoles("admin")
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

    @RequiresRoles("admin")
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

}

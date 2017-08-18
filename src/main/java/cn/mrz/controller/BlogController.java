package cn.mrz.controller;

import cn.mrz.mq.producer.MessageProducer;
import cn.mrz.pojo.Blog;
import cn.mrz.pojo.Comment;
import cn.mrz.pojo.Word;
import cn.mrz.service.BlogService;
import cn.mrz.service.WordService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/6.
 */
@Controller
public class BlogController extends BaseController{

    @Autowired
    MessageProducer messageProducer;

    Logger logger = LoggerFactory.getLogger(BlogController.class);

    final Integer pageSize = 10;

    @Resource
    private BlogService blogService;
    @Resource
    private WordService wordService;

    @RequestMapping(value = {"/admin/blog/go/add"})
    public String goAddBlog(ModelMap map) {
        map.addAttribute("oper", "新增博文");
        return "/admin/blog/edit";
    }

    @RequestMapping(value = {"/admin/blog/go/edit/{id}"})
    public String goEditBlog(ModelMap map, @PathVariable Long id) {
        Blog blog = blogService.getById(id);
        map.addAttribute("oper", "修改博文");
        map.addAttribute("blog", blog);
        return "/admin/blog/edit";
    }

    @RequestMapping(value = "/blog/blog/{id}")
    public String goBlogPage(ModelMap map,@PathVariable Long id) {
        Blog blog = blogService.getById(id);
        List<Comment> commentList = blogService.getCommentByBId(id);
        if (blog == null) {
            throw new RuntimeException("没有这样的博文~~~");
        }
        map.addAttribute("blog", blog);
        map.addAttribute("commentList", JSON.toJSONString(commentList));
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
            throw new RuntimeException("没有这样的博客");
        }
        map.addAttribute("hotWord",hotWord);
        map.addAttribute("hashcode",hashcode);
        return "/blog/hotword";
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
        pagination.setAsc(false);
        pagination = blogService.getBlogList(pagination, false);
        List<Blog> blogList = pagination.getRecords();
        Integer blogCountNum = pagination.getTotal();
        Map map = new HashMap();
        map.put("success",true);
        Map data = new HashMap();
        data.put("blogList",blogList);
        double pageNum = Math.ceil(blogCountNum.doubleValue() / pageSize.doubleValue());
        data.put("pageNum",pageNum);
        data.put("blogCountNum",blogCountNum);
        map.put("data",data);
        return JSONObject.toJSONString(map);
    }

    /**
     * 首页
     * @return
     */
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

    @ResponseBody
    @RequestMapping(value = {"/blog/visit/{blogId}"}, produces = "application/javascript")
    public String visitBlog(@PathVariable Long blogId) {
        boolean addVisit = blogService.addVisit(blogId);
        if(addVisit){
            return "console.log(\"visit success\");";
        }else{
            return "console.log(\"visit fail\");";
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/admin/blog/list/username/{username}"}, produces = {"application/json;charset=UTF-8"})
    public String getBlogListByUsername(@PathVariable String username) {
        Page<Blog> pagination = new Page<Blog>(1, 10);
        pagination = blogService.getUserBlogList(pagination,username);
        List<Blog> userBlogList =pagination.getRecords();
        HashMap data = new HashMap();
        data.put("userBlogList", userBlogList);
        HashMap hashMap = new HashMap();
        hashMap.put("success", true);
        hashMap.put("data", data);
        return JSONObject.toJSONString(hashMap);
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
        //图片都是外链,这个字段GG
        blog.setImageId(0L);
        blog.setClassType(0);
        blogService.addBlog(blog);

        //将要分词的博客添加到消息队列中
        messageProducer.sendSplitWordMessage(blog.getId());

//        final Blog blog2 = blog;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                wordService.getBlogWords(blog2);
//            }
//        }
//        ).start();
        return "{\"success\": true}";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/blog/edit")
    public String editBlog(Blog blog) {

        if (null == blog) {
            return DEFAULT_FAILED_MESSAGE;
        }
        Blog oldBlog = blogService.getById(blog.getId());
        if (null == oldBlog) {
            return DEFAULT_FAILED_MESSAGE;
        }
        blog.setEditDate(new Date(System.currentTimeMillis()));
        blogService.update(blog);

        //将要分词的博客添加到消息队列中
        messageProducer.sendSplitWordMessage(blog.getId());

//        final Blog blog2 = blog;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                wordService.getBlogWords(blog2);
//            }
//        }).start();
        return "{\"success\": true}";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/blog/delete", produces = {"application/json;charset=UTF-8"})
    public String deleteBlog(@RequestParam Long id,@RequestParam(required = false) Integer page) {

        if(page==null)
            page = 1;
        String blogListJson = DEFAULT_FAILED_MESSAGE;
        Blog blog = new Blog();
        blog.setId(id);
        int isDelete = blogService.deleteBlog(blog);
        if(isDelete>0){
            Page<Blog> pagination = new Page<Blog>(page,pageSize,"create_date");
            pagination.setAsc(false);
            pagination= blogService.getBlogList(pagination, false);
            List<Blog> blogList = pagination.getRecords();
            Integer blogCountNum = pagination.getTotal();
            double pageNum = Math.ceil(blogCountNum.doubleValue() / pageSize.doubleValue());
            HashMap data = new HashMap();
            data.put("blogList", blogList);
            data.put("blogCountNum", blogCountNum);
            data.put("pageNum", pageNum);
            HashMap map = new HashMap();
            map.put("success",true);
            map.put("data",data);
            blogListJson = JSONObject.toJSONString(map);
        }
        return blogListJson;
    }

    @ResponseBody
    @RequestMapping(value = "/blog/comment/updown", produces = {"application/json;charset=UTF-8"})
    public String commentUp(@RequestParam Long cId,@RequestParam Long direction) {
        String commentUpJson = DEFAULT_FAILED_MESSAGE;
        if(cId==null)
            return commentUpJson;
        Long id = (Long)SecurityUtils.getSubject().getSession().getAttribute("id");
        int result = blogService.commentUpDown(cId, id, direction);
        HashMap map = new HashMap();
        map.put("success",true);
        map.put("data",result);
        commentUpJson = JSONObject.toJSONString(map);
        return commentUpJson;
    }
}

package cn.mrz.controller;

import cn.mrz.mapper.UserMapper;
import cn.mrz.mapper.VisitMapper;
import cn.mrz.pojo.Blog;
import cn.mrz.pojo.User;
import cn.mrz.service.BlogService;
import cn.mrz.service.BuyService;
import cn.mrz.view.JsonView;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/16.
 */
@Controller
public class SystemController extends BaseController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private VisitMapper visitMapper;
    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = {"/admin/go/{page}"})
    public String goAdminPage(@PathVariable String page) {
        return "/admin/" + page;
    }

    @ResponseBody
    @RequestMapping(value = {"/admin/blogInfo"}, produces = {"application/json;charset=UTF-8"})
    public String getBlogInfo() {
        int blogCountNum = blogService.getBlogCountNum();
        int visitCount = visitMapper.getAllVisitSum();
        Map data = new HashMap();
        data.put("blogCountNum", blogCountNum);
        data.put("visitCount", visitCount);
        Map info = new HashMap();
        info.put("success", true);
        info.put("data", data);
        return JSONObject.toJSONString(info);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam(value = "rememberMe", required = false) Boolean rememberMe, HttpServletRequest request) {

        Subject subject = SecurityUtils.getSubject();
        String url = "/";
        if(!subject.isAuthenticated()) {//&&!subject.isRemembered()
            if (rememberMe == null)
                rememberMe = false;
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            token.setRememberMe(rememberMe);
            try {
                subject.login(token);
            } catch (IncorrectCredentialsException ice) {
                // 捕获密码错误异常
                ModelAndView mv = new ModelAndView("logon");
                mv.addObject("errorMessage", "密码或用户名错误!");
                return mv;
            } catch (UnknownAccountException uae) {
                // 捕获未知用户名异常
                ModelAndView mv = new ModelAndView("logon");
                mv.addObject("errorMessage", "密码或用户名错误!");
                return mv;
            } catch (ExcessiveAttemptsException eae) {
                // 捕获错误登录过多的异常
                ModelAndView mv = new ModelAndView("logon");
                mv.addObject("errorMessage", "尝试次数过多!");
                return mv;
            }
            //通过记住我登录,缺少session中的部分信息
            Session session = subject.getSession();
            session.setAttribute("username", username);
            User user = userMapper.getUserByUsername(username);
            session.setAttribute("id", user.getId());
            //获取到被拦截的页面,丢失锚信息
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            if (savedRequest != null)
                url = savedRequest.getRequestUrl();
        }
        return new ModelAndView("redirect:" + url);
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxlogin", produces = {"application/json;charset=UTF-8"})
    public String ajaxLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        //TODO 通过JsonView,可以和上面的方法整合
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException ice) {
            // 捕获密码错误异常
            return "{\"success\":false,\"message\":\"密码或用户名错误!\"}";
        } catch (UnknownAccountException uae) {
            // 捕获未知用户名异常
            return "{\"success\":false,\"message\":\"密码或用户名错误!\"}";

        } catch (ExcessiveAttemptsException eae) {
            // 捕获错误登录过多的异常
            return "{\"success\":false,\"message\":\"尝试次数过多!\"}";
        }
        Session session = subject.getSession();
        session.setAttribute("username", username);
        User user = userMapper.getUserByUsername(username);
        session.setAttribute("id", user.getId());
        return "{\"success\":true}";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            currentUser.logout();
        }
        return "redirect:/";
    }

    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public String logout(@RequestParam Boolean isAjax) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        if (isAjax) {
            if (currentUser != null) {
                currentUser.logout();
                return "{\"success\":true}";
            }
        }
        return "{\"success\":false}";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/search/search", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public String search(@RequestParam String keyword, @RequestParam(required = false) String subject, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) throws IOException {
        if (subject == null) {
            Page<Blog> pagination = new Page<Blog>(1, 10);
            pagination = blogService.searchBlogByTitle(pagination, keyword);
            List<Blog> blogList = pagination.getRecords();
            Map data = new HashMap();
            data.put("blogList", blogList);
            data.put("dataType", "blog");
            Map info = new HashMap();
            info.put("success", true);
            info.put("data", data);
            return JSONObject.toJSONString(info);
        }
        //聚合
        Page<Blog> pagination = new Page<Blog>(1, 10);
        pagination = blogService.searchBlogByTitle(pagination, keyword);
        List<Blog> blogList = pagination.getRecords();
        Map data = new HashMap();
        data.put("blogList", blogList);
        data.put("dataType", "blog");
        Map info = new HashMap();
        info.put("success", true);
        info.put("data", data);
        return JSONObject.toJSONString(info);
    }


}

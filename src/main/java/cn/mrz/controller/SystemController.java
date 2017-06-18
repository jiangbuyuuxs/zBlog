package cn.mrz.controller;

import cn.mrz.mapper.VisitMapper;
import cn.mrz.pojo.User;
import cn.mrz.service.BlogService;
import cn.mrz.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.codehaus.jackson.map.ObjectMapper;
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
public class SystemController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private VisitMapper visitMapper;
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/admin/go/{page}"})
    public String goAdminPage(@PathVariable String page) {
        return "/admin/" + page;
    }

    @ResponseBody
    @RequestMapping(value = {"/admin/blogInfo"}, produces = {"application/json;charset=UTF-8"})
    public String getBlogInfo() {
        Map<String, Object> info = new HashMap();
        int blogCountNum = blogService.getBlogCountNum();
        int visitCount = visitMapper.getAllVisitSum();
        info.put("blogCountNum", blogCountNum);
        info.put("visitCount", visitCount);
        String infoJson = "{\"success\": false}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            infoJson = mapper.writeValueAsString(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return infoJson;
    }
    @ResponseBody
    @RequestMapping(value = {"/admin/loggeduser"}, produces = {"application/json;charset=UTF-8"})
    public String getLoggedUser() {
        Map<String, Object> info = new HashMap();
        List<String> loggedInUserList = userService.getLoggedInUserList();
        info.put("success", true);
        info.put("loggedInUserList", loggedInUserList);
        info.put("loggedInUserCount", loggedInUserList==null?0:loggedInUserList.size());
        String infoJson;
        ObjectMapper mapper = new ObjectMapper();
        try {
            infoJson = mapper.writeValueAsString(info);
        } catch (IOException e) {
            e.printStackTrace();
            infoJson = "{\"success\": false,\"message\":\"转换数据失败\"}";
        }
        return infoJson;
    }

    @RequestMapping("/login")
    public ModelAndView login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
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
        Session session = subject.getSession();
        session.setAttribute("username", username);
        //获取到被拦截的页面,丢失锚信息
        SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        String url = "/";
        if (savedRequest != null)
            url = savedRequest.getRequestUrl();
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            //注销之后,redis中的session还有300s的失效时间
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

}

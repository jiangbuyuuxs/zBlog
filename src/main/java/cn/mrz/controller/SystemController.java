package cn.mrz.controller;

import cn.mrz.dao.VisitDao;
import cn.mrz.pojo.User;
import cn.mrz.pojo.Visit;
import cn.mrz.service.BlogsService;
import cn.mrz.service.UsersService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sun.org.glassfish.gmbal.ParameterNames;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/16.
 */
@Controller
public class SystemController {

    @Autowired
    private BlogsService blogsService;
    @Autowired
    private VisitDao visitDao;
    @Autowired
    private UsersService userService;

    @RequestMapping(value = {"/admin/go/{page}"})
    public String goAdminPage(@PathVariable String page) {
        return "/admin/" + page;
    }

    @ResponseBody
    @RequestMapping(value = {"/admin/blogInfo"}, produces = {"application/json;charset=UTF-8"})
    public String getBlogInfo() {
        Map<String, Object> infos = new HashMap();
        int blogNums = blogsService.getBlogNums();
        int visitCount = visitDao.getAllVisitSum();
        infos.put("blogsCount", blogNums);
        infos.put("visitCount", visitCount);

        String infoJson = "{\"success\": false}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            infoJson = mapper.writeValueAsString(infos);
        } catch (IOException e) {
            e.printStackTrace();
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
        User user = userService.getUserByUsername(username);
        Session session = subject.getSession();
        session.setAttribute("user", user);
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

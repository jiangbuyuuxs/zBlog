package cn.mrz.controller;

import cn.mrz.pojo.User;
import cn.mrz.service.UserService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/6.
 */
@Controller
public class UserController {

    @Resource
    private UserService usersService;

    @ResponseBody
    @RequestMapping(value = "/admin/user/{page}/page", produces = {"application/json;charset=UTF-8"})
    public String getUsersInfo(@PathVariable int page) {
        int pageSize = 10;
        int start = (page - 1) * 10;
        List<User> users = usersService.getUsers(start, pageSize, null);
        ObjectMapper mapper = new ObjectMapper();
        String usersJson = null;
        try {
            usersJson = mapper.writeValueAsString(users);
        } catch (IOException e) {
            e.printStackTrace();
            usersJson = "{\"success\": false}";
        }
        return usersJson;
    }

    @RequiresRoles("admin")
    @ResponseBody
    @RequestMapping(value = "/admin/user/{username}/del/{page}/page", produces = {"application/json;charset=UTF-8"})
    public String getUsersinfo(@PathVariable String username, @PathVariable int page) {
        String usersJson = null;
        User delUser = usersService.getUserByUsername(username);
        if (delUser == null) {
            usersJson = "{\"success\": false,\"msg\":\"未找到需要删除的用户\"}";
            return usersJson;
        }
        boolean delNum = usersService.delete(delUser.getUsername());
        if (!delNum) {
            usersJson = "{\"success\": false,\"msg\":\"未成功删除用户\"}";
            return usersJson;
        }
        int pageSize = 10;
        int start = (page - 1) * 10;
        int end = start + pageSize;
        List<User> users = usersService.getUsers(start, end, null);
        ObjectMapper mapper = new ObjectMapper();
        try {
            usersJson = mapper.writeValueAsString(users);
        } catch (IOException e) {
            e.printStackTrace();
            usersJson = "{\"success\": false}";
        }
        return usersJson;
    }

    @ResponseBody
    @RequestMapping(value = "/admin/user/exist", produces = {"application/json;charset=UTF-8"})
    public String existUser(@RequestParam String username) {
        User userByUsername = usersService.getUserByUsername(username);
        if(userByUsername==null){
            return "true";
        }
        return "false";
    }

    @RequiresRoles("admin")
    @ResponseBody
    @RequestMapping(value = "/admin/user/add", produces = {"application/json;charset=UTF-8"})
    public String existUser(@Valid User user, BindingResult bingingresult) {

        if(bingingresult.hasErrors()){

            return "{\"success\": false}";
        }
//        boolean addUserResult = usersService.addUser(user);
//        if(addUserResult){
//            return "{\"success\": true}";
//        }
        return "{\"success\": false}";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/user/{username}/userinfo", produces = {"application/json;charset=UTF-8"})
    public String userInfo(@PathVariable String username) {
        User user = usersService.getUserByUsername(username);
        String userJson = "{\"success\": false}";
        if(user!=null) {
            user.setPassword(null);
            Map map = new HashMap();
            map.put("success", true);
            map.put("userInfo", user);
            try {
                userJson = new ObjectMapper().writeValueAsString(map);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return userJson;
    }



}

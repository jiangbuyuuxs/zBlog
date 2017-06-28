package cn.mrz.controller;

import cn.mrz.pojo.User;
import cn.mrz.service.UserService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/6.
 */
@Controller
public class UserController extends BaseController{

    @Resource
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/admin/user/list", produces = {"application/json;charset=UTF-8"})
    public String getUserInfoList(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer pageSize) {
        if(page==null)
            page = 1;
        if(pageSize==null)
            pageSize = 10;
        Page<User> Pagination= userService.getUserList(new Page<User>(page,pageSize));
        List<User> userList = Pagination.getRecords();
        Map data = new HashMap();
        data.put("userList",userList);

        Map map = new HashMap();
        map.put("success",true);
        map.put("data",data);
        return JSONObject.toJSONString(map);
    }

    @ResponseBody
    @RequestMapping(value = "/admin/user/delete", produces = {"application/json;charset=UTF-8"})
    public String delUser(@RequestParam String username, @RequestParam(required = false) Integer page) {
        if(page==null)
            page = 1;
        String usersJson;
        User delUser = userService.getUserByUsername(username);
        if (delUser == null) {
            usersJson = "{\"success\": false,\"message\":\"未找到需要删除的用户\"}";
            return usersJson;
        }
        boolean delNum = userService.delete(delUser.getUsername());
        if (!delNum) {
            usersJson = "{\"success\": false,\"message\":\"未成功删除用户\"}";
            return usersJson;
        }
        int pageSize = 10;
        Page<User> pagination = userService.getUserList(new Page<User>(page, pageSize));

        List<User> userList = pagination.getRecords();
        Map data = new HashMap();
        data.put("userList",userList);
        Map map = new HashMap();
        map.put("success",true);
        map.put("data",data);

        return JSONObject.toJSONString(map);
    }

    /**
     * 表单验证
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/admin/user/exist", produces = {"application/json;charset=UTF-8"})
    public String existUser(@RequestParam String username) {
        User userByUsername = userService.getUserByUsername(username);
        if (userByUsername == null) {
            return "true";
        }
        return "false";
    }

    @ResponseBody
    @RequestMapping(value = "/admin/user/add", produces = {"application/json;charset=UTF-8"})
    public String addUser(@Valid User user, BindingResult bingingresult) {
        if (bingingresult.hasErrors()) {
            return "{\"success\": false,\"message\":"+bingingresult.getFieldErrors().get(0).toString()+"}";
        }
        boolean addUserResult = userService.addUser(user);
        if(addUserResult){
            return "{\"success\": true}";
        }
        return "{\"success\": false}";
    }
    @ResponseBody
    @RequestMapping(value = "/admin/user/edit", produces = {"application/json;charset=UTF-8"})
    public String editUser(@RequestParam String nickname,@RequestParam String username) {
        boolean result = userService.updateNickname(username,nickname);
        Map map = new HashMap();
        map.put("success", result);
        return JSONObject.toJSONString(map);
    }
    @ResponseBody
    @RequestMapping(value = "/admin/user/enabled", produces = {"application/json;charset=UTF-8"})
    public String enabledUser(@RequestParam String username) {
//        try {
            int enabled = userService.changeState(username);
            Map data = new HashMap();
            data.put("enabled", enabled);
            data.put("username", username);
            Map map = new HashMap();
            map.put("success", true);
            map.put("data", data);
            return JSONObject.toJSONString(map);
//        }catch (RuntimeException e){
//            Map map = new HashMap();
//            map.put("success", false);
//            map.put("message", e.getMessage());
//            return JSONObject.toJSONString(map);
//        }
    }

    @ResponseBody
    @RequestMapping(value = "/admin/user/username/{username}", produces = {"application/json;charset=UTF-8"})
    public String getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            user.setPassword(null);
            Map data = new HashMap();
            data.put("userInfo", user);
            Map map = new HashMap();
            map.put("success", true);
            map.put("data", data);
            return JSONObject.toJSONString(map);
        }else{
            Map map = new HashMap();
            map.put("success", false);
            map.put("message", "没找到这样的用户");
            return JSONObject.toJSONString(map);
        }
    }

    @ResponseBody
    @RequestMapping(value = {"/admin/loggeduser"}, produces = {"application/json;charset=UTF-8"})
    public String getLoggedUser() {
        Map loggedUserMap = userService.getLoggedInUserList();
        List<String> loggedInUserList = (List<String>)loggedUserMap.get("user");
        int unLoggedUserNum = (Integer)loggedUserMap.get("unLoggedNum");

        Map data = new HashMap();
        data.put("loggedInUserList", loggedInUserList);
        data.put("unLoggedUserNum", unLoggedUserNum);
        data.put("loggedInUserCount", loggedInUserList == null ? 0+unLoggedUserNum : loggedInUserList.size()+unLoggedUserNum);

        Map info = new HashMap();
        info.put("success", true);
        info.put("data", data);

        return JSONObject.toJSONString(info);
    }
}

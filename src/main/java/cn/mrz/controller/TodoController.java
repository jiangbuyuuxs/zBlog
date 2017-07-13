package cn.mrz.controller;

import cn.mrz.pojo.Todo;
import cn.mrz.service.TodoService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/6.
 */
@Controller
public class TodoController extends BaseController{

    Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Resource
    private TodoService todoService;

    @ResponseBody
    @RequestMapping(value = "/admin/todo/list", produces = {"application/json;charset=UTF-8"})
    public String getTodoList(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer pageSize) {

        if(page==null)
            page =1;
        if(pageSize==null)
            pageSize = 10;
        Page<Todo> pagination = new Page<Todo>(page,pageSize);

        pagination = todoService.list(pagination);

        List<Todo> todoList = pagination.getRecords();
        int total = pagination.getTotal();
        Map data = new HashMap();
        data.put("todoList",todoList);
        data.put("total",total);

        Map map = new HashMap();
        map.put("success",true);
        map.put("data",data);
        return JSONObject.toJSONStringWithDateFormat(map, "MM月dd E HH:mm:ss");
    }
    @ResponseBody
    @RequestMapping(value = "/admin/todo/add", produces = {"application/json;charset=UTF-8"})
    public String addTodo(@RequestParam(required = false) String title,@RequestParam(required = false) String remark) {

        if(title==null||"".equals(title))
            title = "新的Todo";

        Date date = new Date(System.currentTimeMillis());

        Todo todo = new Todo(title,remark,date,0,1L);

        todoService.add(todo);

        Page<Todo> pagination = new Page<Todo>(1,10);

        pagination = todoService.list(pagination);
        List<Todo> todoList = pagination.getRecords();
        int total = pagination.getTotal();
        Map data = new HashMap();
        data.put("todoList",todoList);
        data.put("total",total);

        Map map = new HashMap();
        map.put("success",true);
        map.put("data", data);
        return JSONObject.toJSONStringWithDateFormat(map, "MM月dd E a");
    }

}

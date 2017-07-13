package cn.mrz.service.impl;

import cn.mrz.mapper.TodoMapper;
import cn.mrz.pojo.Todo;
import cn.mrz.service.TodoService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
@Service
@Transactional
public class TodoServiceImpl implements TodoService {

    @Autowired
    TodoMapper todoMapper;

    public Page<Todo> list(Page<Todo> pagination) {
        EntityWrapper<Todo> todoEntityWrapper = new EntityWrapper<Todo>();
//        todoEntityWrapper;
        todoEntityWrapper.orderBy("create_date", false);
        List<Todo> todoList = todoMapper.selectPage(pagination, todoEntityWrapper);
        pagination.setRecords(todoList);
        return pagination;
    }

    @Override
    public void add(Todo todo) {
        Long id = todoMapper.insertTodo(todo);
    }

    @Override
    public void update(Todo todo) {

    }

    @Override
    public Todo getById(Long id) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}



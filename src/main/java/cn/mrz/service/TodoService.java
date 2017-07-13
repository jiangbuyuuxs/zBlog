package cn.mrz.service;

import cn.mrz.pojo.Todo;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * Created by Administrator on 2017/7/13.
 */
public interface TodoService extends BaseService<Todo,Long> {
    public Page<Todo> list(Page<Todo> pagination);

    void add(Todo todo);

}

package cn.mrz.service;

import cn.mrz.pojo.Todo;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * Created by Administrator on 2017/7/13.
 */
public interface TodoService extends BaseService<Todo,Long> {
    Page<Todo> list(Page<Todo> pagination,Integer state);

    void add(Todo todo);

    void complete(Long id);

}

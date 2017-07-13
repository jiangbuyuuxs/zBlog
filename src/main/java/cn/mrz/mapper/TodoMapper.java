package cn.mrz.mapper;

import cn.mrz.pojo.Todo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/7/13.
 */
@Repository
public interface TodoMapper extends BaseMapper<Todo> {
    Long insertTodo(Todo todo);
}

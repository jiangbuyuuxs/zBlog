package cn.mrz.mapper;

import cn.mrz.pojo.Comment;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> selectCommentByBId(Long bId);
}

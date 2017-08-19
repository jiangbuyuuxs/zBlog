package cn.mrz.mapper;

import cn.mrz.pojo.Comment;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> selectCommentByBId(Long bId);

    void updateUp(@Param("up")Long up, @Param("id")Long id);
    void updateDown(@Param("down")Long down, @Param("id")Long id);

    Integer insertComment(Comment comment);
}

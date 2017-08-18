package cn.mrz.mapper;

import cn.mrz.pojo.CommentUpDown;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/8/18.
 */
@Repository
public interface CommentUpDownMapper extends BaseMapper<CommentUpDown> {

    CommentUpDown findByCIdUserId(@Param("cId")Long cId, @Param("userId")Long userId,@Param("direction") Long direction);

    void addCommentUpDown(CommentUpDown commentUpDown);
}

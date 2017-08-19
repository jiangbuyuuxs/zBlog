package cn.mrz.mapper;

import cn.mrz.pojo.Reply;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/8/17.
 */
@Repository
public interface ReplyMapper extends BaseMapper<Reply> {

    Reply findReplyByCId(Long cId);

    Integer insertReply(Reply reply);
}

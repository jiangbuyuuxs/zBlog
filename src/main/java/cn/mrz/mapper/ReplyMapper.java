package cn.mrz.mapper;

import cn.mrz.pojo.Reply;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * Created by Administrator on 2017/8/17.
 */
public interface ReplyMapper extends BaseMapper<Reply> {

    Reply findReplyByCId(Long cId);
}

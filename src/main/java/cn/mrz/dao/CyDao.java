package cn.mrz.dao;

import cn.mrz.pojo.Cy;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/4/20.
 */
@Repository
public interface CyDao   extends BaseMapper<Cy> {
    List<Cy> getCyByPyFirst(String pyFirst);
    List<Cy> getCyByPyEnd(String pyEnd);
    Cy getCy(String cy);
}

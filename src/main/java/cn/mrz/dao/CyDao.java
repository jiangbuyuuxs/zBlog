package cn.mrz.dao;

import cn.mrz.pojo.Cy;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/4/20.
 */
public interface CyDao   extends BaseMapper<Cy> {
    List<Cy> getCyByPyfirst(@Param("pyfirst")String pyFirst);
    List<Cy> getCyByPyend(@Param("pyend")String pyEnd);
    Cy getCy(String cy);
}

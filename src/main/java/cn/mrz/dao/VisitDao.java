package cn.mrz.dao;

import cn.mrz.pojo.Visit;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
@Repository
public interface VisitDao  extends BaseMapper<Visit> {
    List<Visit> getHotBlog(int num);
    Visit getVisitByBlogid(Long blogid);
    int updateVisit(@Param("visit")Visit visit);
    int getAllVisitSum();
    int deleteById(Long id);
}

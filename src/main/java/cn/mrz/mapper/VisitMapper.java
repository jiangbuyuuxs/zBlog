package cn.mrz.mapper;

import cn.mrz.pojo.Visit;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */
@Repository
public interface VisitMapper extends BaseMapper<Visit> {

    List<Visit> selectVisitList(Pagination page);

    Visit getVisitByBlogId(Long blogId);
    int updateVisit(@Param("visit")Visit visit);
    int getAllVisitSum();
    int deleteById(Long id);
}

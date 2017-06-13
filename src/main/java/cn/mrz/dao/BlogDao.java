package cn.mrz.dao;

import cn.mrz.pojo.Blog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public interface BlogDao extends BaseMapper<Blog> {
    List<Blog> getBlogs(@Param("start")int start, @Param("num")int num, @Param("sort")String sort);
    List<Blog> getBlogsWithoutContent(@Param("start")int start, @Param("num")int num, @Param("sort")String sort);
    int selectCount();
    int deleteById(Long id);
    Long insertBlog(Blog blog);
}

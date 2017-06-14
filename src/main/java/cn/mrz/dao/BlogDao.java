package cn.mrz.dao;

import cn.mrz.pojo.Blog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
@Repository
public interface BlogDao extends BaseMapper<Blog> {
    List<Blog> selectBlogList(Pagination page);
    List<Blog> selectBlogListWithoutContent(Pagination page);
    int selectCount();
    int deleteById(Long id);
    Long insertBlog(Blog blog);
}

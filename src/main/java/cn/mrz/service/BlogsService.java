package cn.mrz.service;

import cn.mrz.pojo.Blog;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public interface BlogsService extends BaseService<Blog,Long> {
    List<Blog> getBlogs(int start, int num, String orderByNum, boolean hasContent);

    int getBlogNums();

    boolean addVisit(long blogidid);

    void addBlog(Blog blog);

    List<Blog> getHotBlogs(int blogNums);

    boolean deleteBlogById(Long id);
}

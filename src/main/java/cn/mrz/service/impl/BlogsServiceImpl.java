package cn.mrz.service.impl;

import cn.mrz.dao.BlogDao;
import cn.mrz.dao.VisitDao;
import cn.mrz.dao.WordDao;
import cn.mrz.pojo.Blog;
import cn.mrz.pojo.Visit;
import cn.mrz.pojo.Word;
import cn.mrz.service.BlogsService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
@Service
public class BlogsServiceImpl implements BlogsService {
    @Autowired
    private BlogDao blogDao;

    @Autowired
    private VisitDao visitDao;
    @Autowired
    private WordDao wordDao;

    /**
     * 获取文章列表信息
     *
     * @param start 开始篇数
     * @param num   总篇数
     * @return 博文信息, 不包含
     */
    public List<Blog> getBlogs(int start, int num, String orderByNum, boolean hasContent) {
        if (!hasContent) {
            return blogDao.getBlogsWithoutContent(start, num, orderByNum);
        }else
            return blogDao.getBlogs(start, num, orderByNum);
    }

    @Override
    public int getBlogNums() {
        return blogDao.selectCount();
    }

    @Override
    public boolean addVisit(long blogid) {
        try {
            Visit visit = visitDao.getVisitByBlogid(blogid);
            if (null == visit) {
                visit = new Visit();
                visit.setBlogid(blogid);
                visit.setNum(1);
                visitDao.insert(visit);
            } else {
                visit.setNum(visit.getNum() + 1);
                visitDao.updateVisit(visit);
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @Override
    public void addBlog(Blog blog) {
        blogDao.insertBlog(blog);
        long blogid = blog.getId();
        Visit visit = new Visit();
        visit.setBlogid(blogid);
        visit.setNum(0);
        visitDao.insert(visit);
    }

    @Override
    public List<Blog> getHotBlogs(int blogNums) {
        List<Visit> hotBlogs = visitDao.getHotBlog(blogNums);
        ArrayList<Blog> blogs = new ArrayList<Blog>();
        for (Visit hotBlog : hotBlogs) {
            long blogid = hotBlog.getBlogid();
            Blog hotblogs = blogDao.selectById(blogid);
            blogs.add(hotblogs);
        }
        return blogs;
    }

    @Override
    public boolean deleteBlogById(Long id) {
        int deleteVisit = visitDao.deleteById(id);
        int deleteBlog = blogDao.deleteById(id);
        int deleteWord = wordDao.delWordsByBlogid(id);
        return deleteBlog==1;
    }

    public void update(Blog blog) {
        blogDao.updateById(blog);
    }

    public Blog getById(Long id) {
        return blogDao.selectById(id);
    }

    public boolean delete(Long id) {
        return blogDao.deleteById(id) == 1;
    }

}

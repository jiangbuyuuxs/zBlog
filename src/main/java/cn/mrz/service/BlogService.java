package cn.mrz.service;

import cn.mrz.pojo.Blog;
import cn.mrz.pojo.Word;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public interface BlogService extends BaseService<Blog,Long> {

    Page<Blog> getBlogList(Page<Blog> page,Boolean isAsc, boolean hasContent);



    /**
     * 获取博客总量
     * @return
     */
    int getBlogCountNum();

    boolean addVisit(long blogId);

    void addBlog(Blog blog);

    /**
     * 获取访问量最高的几个博客
     * @param num  几个
     * @return
     */
    List<Blog> getHotBlogList(int num);

    boolean deleteBlogById(Long id);

    /**
     * 获取热词对应的博文,携带包含热词的部分文本
     * @param wordList
     * @return
     */
    List<Blog> getBlogListByWordList(List<Word> wordList);
}

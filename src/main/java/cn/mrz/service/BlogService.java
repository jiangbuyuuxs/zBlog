package cn.mrz.service;

import cn.mrz.pojo.Blog;
import cn.mrz.pojo.Word;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public interface BlogService extends BaseService<Blog,Long> {

    boolean addVisit(Long blogId);

    void addBlog(Blog blog);

    /**
     * 传入Blog对象,如果id不为空使用id为条件删除
     * @param blog
     * @return 删除数量
     */

    int deleteBlog(Blog blog);

    Page<Blog> getBlogList(Page<Blog> page,boolean hasContent);

    /**
     * 获取访问量最高的几个博客
     * @param num  几个
     * @return
     */
    List<Blog> getHotBlogList(int num);


    /**
     * 获取热词对应的博文,携带包含热词的部分文本
     * @param wordList
     * @return
     */
    List<Blog> getBlogListByWordList(List<Word> wordList);

    Page<Blog> getUserBlogList(Page<Blog> page,String author);

    Page<Blog> searchBlogByTitle(Page<Blog> page,String keyword);

    /**
     * 获取总博客数量
     * @return
     */
    int getBlogCountNum();
}

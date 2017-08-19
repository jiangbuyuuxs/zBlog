package cn.mrz.service;

import cn.mrz.pojo.Blog;
import cn.mrz.pojo.Comment;
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

    List<Comment> getCommentByBId(Long bId);

    /**
     * 顶踩评论
     * @param cId 评论id
     * @param userId 执行人id
     * @param direction 0踩1顶
     * @return 顶/踩 变化数
     */
    int commentUpDown(Long cId, Long userId,Long direction);


    /**
     *
     * @param uId
     * @param bId
     * @param content
     * @param device
     * @return
     */
    boolean newComment(Long uId, Long bId, String content,Long device);

    boolean replyComment(Long uId, Long cId, String content,Long device);
}

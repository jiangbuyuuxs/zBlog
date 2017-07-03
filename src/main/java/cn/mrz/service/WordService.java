package cn.mrz.service;

import cn.mrz.pojo.Blog;
import cn.mrz.pojo.Word;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
public interface WordService extends BaseService<Word,Long> {

    /**
     * 使用次数最高的词汇
     * @param num
     * @return
     */
    List<Word> getTopHotWordList(int num);

    Page<Word> getHotWordList(Page<Word> page);

    /**
     * 获取多个博客的分词
     * @param blogList
     */
    void getBlogWords(List<Blog> blogList);

    /**
     * 获取单个博客的分词
     * @param blog
     */
    void getBlogWords(Blog blog);

    Page<Word> getWordsByWordHash(Page<Word> page,String hashcode);
}

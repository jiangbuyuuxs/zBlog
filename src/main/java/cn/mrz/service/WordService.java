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

    List<Word> getHotWordList(int current, int pageSize);

    void getBlogWords(List<Blog> blogList);
    void getBlogWords(Blog blog);

    public Page<Word> getWordsByWordHash(Page<Word> page,String hashcode);
}

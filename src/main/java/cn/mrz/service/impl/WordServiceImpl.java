package cn.mrz.service.impl;

import cn.mrz.mapper.WordMapper;
import cn.mrz.pojo.Blog;
import cn.mrz.pojo.Word;
import cn.mrz.service.WordService;
import com.baomidou.mybatisplus.plugins.Page;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/3/16.
 */
@Transactional
@Service
public class WordServiceImpl implements WordService {


    private class TermWarp {
        public Term term;
        public int num;

        public TermWarp(Term term, int num) {
            this.term = term;
            this.num = num;
        }
    }

    @Autowired
    private WordMapper wordMapper;

    @Override
    public void update(Word word) {
        wordMapper.updateById(word);
    }

    @Override
    public Word getById(Long id) {
        return wordMapper.selectById(id);
    }

    @Override
    public boolean delete(Long id) {
        return wordMapper.deleteById(id)==1;
    }

    @Override
    public void getBlogWords(Blog blog) {
        StopRecognition filter = new StopRecognition();
        filter.insertStopRegexes(".");
        getBlogWords(blog, filter);
    }

    @Override
    public Page<Word> getWordsByWordHash(Page<Word> page,String hashcode) {
        page.setRecords(wordMapper.getWordsByWordHash(page, hashcode));
        return page;
    }

    @Override
    public void getBlogWords(List<Blog> blogList) {
        StopRecognition filter = new StopRecognition();
        filter.insertStopRegexes(".");
        for (Blog blog : blogList) {
            getBlogWords(blog, filter);
        }
    }

    private void getBlogWords(Blog blog, StopRecognition filter) {
        Map<String, TermWarp> termNumMap = new HashMap<String, TermWarp>();
        StringBuffer text = new StringBuffer();
        text.append(blog.getTitle());
        String blogTexts = blog.getTexts();
        //这里的文章内容中包含样式,需要使用正则去掉
        //blogTexts.replaceAll("<[^>]*>", "");
        blogTexts = blogTexts.replaceAll("<[^>]*>|&quot;|&lt;|/*+&gt;|&nbsp;", "");
        text.append(blogTexts);
        Result result = ToAnalysis.parse(text.toString()).recognition(filter);
        List<Term> terms = result.getTerms();
        //统计各个词汇的次数
        for (Term term : terms) {
            String name = term.getName();
            if (termNumMap.containsKey(name)) {
                termNumMap.get(name).num++;
            } else {
                TermWarp termWarp = new TermWarp(term, 1);
                termNumMap.put(name, termWarp);
            }
        }
        saveWords(blog.getId(), termNumMap);
    }

    private void saveWords(long blogId, Map<String, TermWarp> termNumMap) {
        wordMapper.delWordsByBlogId(blogId);
        Set<Map.Entry<String, TermWarp>> entries = termNumMap.entrySet();
        for (Map.Entry<String, TermWarp> wordTerm : entries) {
            String name = wordTerm.getKey();
            TermWarp termWarp = wordTerm.getValue();
            Word word = new Word();
            word.setBlogId(blogId);
            word.setNum(termWarp.num);
            word.setRemark(name);
            word.setWordType(termWarp.term.getNatureStr());
            word.setHashcode("" + name.hashCode());
            wordMapper.insert(word);
        }
    }


    @Override
    public List<Word> getTopHotWordList(int num) {
        return getHotWordList(new Page<Word>(1,num)).getRecords();
    }

    @Override
    public Page<Word> getHotWordList(Page<Word> page) {
        page.setRecords(wordMapper.getWordList(page));
        return page;
    }
}

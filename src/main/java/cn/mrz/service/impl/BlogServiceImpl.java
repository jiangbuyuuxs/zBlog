package cn.mrz.service.impl;

import cn.mrz.mapper.BlogMapper;
import cn.mrz.mapper.VisitMapper;
import cn.mrz.mapper.WordMapper;
import cn.mrz.pojo.Blog;
import cn.mrz.pojo.Visit;
import cn.mrz.pojo.Word;
import cn.mrz.service.BlogService;
import cn.mrz.utils.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private VisitMapper visitMapper;
    @Autowired
    private WordMapper wordMapper;
    @Override
    public Page<Blog> getBlogList(Page<Blog> page,Boolean isAsc,boolean hasContent) {
        if(isAsc!=null)
            page.setAsc(isAsc);
        if (!hasContent) {
            page.setRecords(blogMapper.selectBlogListWithoutContent(page));
            return page;
        }else
            page.setRecords(blogMapper.selectBlogList(page));
            return page;
    }

    @Override
    public int getBlogCountNum() {
        return blogMapper.selectCount();
    }

    @Override
    public boolean addVisit(long blogId) {
        try {
            Visit visit = visitMapper.getVisitByBlogid(blogId);
            if (null == visit) {
                visit = new Visit(blogId,1);
                visitMapper.insert(visit);
            } else {
                visit.setNum(visit.getNum() + 1);
                visitMapper.updateVisit(visit);
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    @Override
    public void addBlog(Blog blog) {
        blogMapper.insertBlog(blog);
        long blogId = blog.getId();
        Visit visit = new Visit(blogId,0);
        visitMapper.insert(visit);
    }

    @Override
    public List<Blog> getHotBlogList(int num) {
        List<Visit> hotBlogVisitList = visitMapper.selectVisitList(new Page<Visit>(1, num));
        ArrayList<Blog> hotBlogList = new ArrayList<Blog>();
        for (Visit hotBlogVisit : hotBlogVisitList) {
            long blogId = hotBlogVisit.getBlogId();
            Blog hotBlog = blogMapper.selectById(blogId);
            hotBlogList.add(hotBlog);
        }
        return hotBlogList;
    }

    @Override
    public boolean deleteBlogById(Long id) {
        int deleteVisit = visitMapper.deleteById(id);
        int deleteBlog = blogMapper.deleteById(id);
        int deleteWord = wordMapper.delWordsByBlogId(id);
        return deleteBlog==1;
    }

    @Override
    public List<Blog> getBlogListByWordList(List<Word> wordList) {
        List<Blog> blogList = new ArrayList<Blog>();
        for (Word word:wordList){
            Blog blog = blogMapper.selectById(word.getBlogId());
            String remark = word.getRemark();
            blog = getHotwordPart(blog, remark);
            blogList.add(blog);
        }
        return blogList;
    }

    @Override
    public List<Blog> getUserBlogList(String author) {
        EntityWrapper<Blog> blogEntityWrapper = new EntityWrapper<Blog>();
        blogEntityWrapper
                .setSqlSelect("id,title,create_date")
                .where("author={0}",author)
                .orderBy("create_date",false);
        Page page = new Page(1, 10);
        return blogMapper.selectPage(page,blogEntityWrapper);
    }

    private Blog getHotwordPart(Blog blog, String remark) {
        //截取的文章长度
        final int textsSubLength = 100;
        String texts = blog.getTexts();
        texts = StringUtils.removeTag(texts);
        int textsLength = texts.length();
        int indexOf = texts.indexOf(remark);
        if(indexOf==-1){
            //正文部分没有热词,就显示前面100一些好了
            if(textsLength>100){
                texts = texts.substring(0,textsSubLength);
            }
        }else{
            if(textsLength>textsSubLength){
                if(indexOf<50) {
                    texts = texts.substring(0, textsSubLength);
                }else if(indexOf>50){
                    if(indexOf+50>textsLength){
                        //并没有那么长
                        texts =texts.substring(textsLength-textsSubLength,textsLength);
                    }else{
                        texts = texts.substring(indexOf-50, indexOf + 50);
                    }
                }

            }
        }
        blog.setTexts(texts);
        return blog;
    }


    public void update(Blog blog) {
        blogMapper.updateById(blog);
    }

    public Blog getById(Long id) {
        return blogMapper.selectById(id);
    }

    public boolean delete(Long id) {
        return blogMapper.deleteById(id) == 1;
    }

}

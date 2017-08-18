package cn.mrz.service.impl;

import cn.mrz.mapper.*;
import cn.mrz.pojo.*;
import cn.mrz.service.BlogService;
import cn.mrz.utils.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
@Transactional
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private VisitMapper visitMapper;
    @Autowired
    private WordMapper wordMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentUpDownMapper commentUpDownMapper;
    @Override
    public Page<Blog> getBlogList(Page<Blog> page,boolean hasContent) {
        if (!hasContent) {
            page.setRecords(blogMapper.selectBlogListWithoutContent(page));
            return page;
        }else {
            page.setRecords(blogMapper.selectBlogList(page));
            return page;
        }
    }

    @Override
    public boolean addVisit(Long blogId) {
        try {
            Visit visit = visitMapper.getVisitByBlogId(blogId);
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
        Page<Visit> pagination = new Page<Visit>(1, num,"num");
        pagination.setAsc(false);
        List<Visit> hotBlogVisitList = visitMapper.selectVisitList(pagination);
        ArrayList<Blog> hotBlogList = new ArrayList<Blog>();
        for (Visit hotBlogVisit : hotBlogVisitList) {
            long blogId = hotBlogVisit.getBlogId();
            Blog hotBlog = blogMapper.selectBlogWithoutContent(blogId);
            hotBlogList.add(hotBlog);
        }
        return hotBlogList;
    }

    @Override
    public int deleteBlog(Blog blog) {
        if(blog!=null){
            Long id = blog.getId();
            int deleteVisit = visitMapper.deleteById(id);
            int deleteBlog = blogMapper.deleteById(id);
            int deleteWord = wordMapper.delWordsByBlogId(id);
            return deleteBlog;
        }else{
            return 0;
        }
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
    public Page<Blog> getUserBlogList(Page<Blog> page,String author) {
        EntityWrapper<Blog> blogEntityWrapper = new EntityWrapper<Blog>();
        blogEntityWrapper
                .setSqlSelect("id,title,create_date")
                .where("author={0}",author)
                .orderBy("create_date",false);
        page.setRecords(blogMapper.selectPage(page,blogEntityWrapper));
        return page;
    }

    @Override
    public Page<Blog> searchBlogByTitle(Page<Blog> page,String keyword) {
        EntityWrapper<Blog> blogEntityWrapper = new EntityWrapper<Blog>();
        blogEntityWrapper
                .setSqlSelect("id,title,edit_date")
                .where("title like {0}","%"+keyword+"%")
                .orderBy("create_date",false);

        page.setRecords(blogMapper.selectPage(page, blogEntityWrapper));
        return page;

    }

    @Override
    public int getBlogCountNum() {
        return blogMapper.selectCount();
    }

    @Override
    public List<Comment> getCommentByBId(Long bId) {
        return commentMapper.selectCommentByBId(bId);
    }

    @Override
    public int commentUpDown(Long cId, Long userId,Long direction) {
        int result = 0;
        CommentUpDown commentUpDown = commentUpDownMapper.findByCIdUserId(cId,userId, direction);
        if(commentUpDown==null){
            commentUpDown = new CommentUpDown(cId, userId, direction);
            commentUpDownMapper.addCommentUpDown(commentUpDown);
            result = 1;
        }else{
            Long flag = commentUpDown.getFlag();
            if(flag==0){
                commentUpDown.setFlag(1L);
                commentUpDownMapper.updateById(commentUpDown);
                result = 1;
            }else if(flag==1){
                commentUpDown.setFlag(0L);
                commentUpDownMapper.updateById(commentUpDown);
                result = -1;
            }
        }
        //找出当前点赞的个数
        EntityWrapper<CommentUpDown> commentUpDownEntityWrapper = new EntityWrapper<CommentUpDown>();
        commentUpDownEntityWrapper.eq("c_id",cId);
        commentUpDownEntityWrapper.eq("direction",direction);
        commentUpDownEntityWrapper.eq("flag",1);
        Long directionCount = new Long(commentUpDownMapper.selectCount(commentUpDownEntityWrapper));
        //更新评论表
        if(direction==1){
            commentMapper.updateUp(directionCount, cId);
        }else if(direction==0){
            commentMapper.updateDown(directionCount, cId);
        }
        return result;
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


    @Deprecated
    public boolean delete(Long id) {
        return blogMapper.deleteById(id) == 1;
    }

}

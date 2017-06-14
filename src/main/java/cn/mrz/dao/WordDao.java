package cn.mrz.dao;

import cn.mrz.pojo.Word;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
@Repository
public interface WordDao extends BaseMapper<Word> {
    List<Word> getWordList(Pagination page);

    int delAll();

    Word getWordByName(String name);

    int delWordsByBlogId(long blogId);

    List<Word> getWordsByWordHash(@Param("hashcode")String hashcode);
}

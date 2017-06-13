package cn.mrz.dao;

import cn.mrz.pojo.Word;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
public interface WordDao extends BaseMapper<Word> {
    List<Word> getWords(@Param("start")int start, @Param("num")int num);

    int delAll();

    Word getWordByName(String name);

    int delWordsByBlogid(long blogid);

    List<Word> getWordsByWordHash(@Param("hashcode")String hashcode);
}

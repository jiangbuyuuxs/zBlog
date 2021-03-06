package cn.mrz.mapper;

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
public interface WordMapper extends BaseMapper<Word> {
    List<Word> getWordList(Pagination page);

    int delAll();

    Word getWordByName(String name);

    int delWordsByBlogId(long blogId);

    List<Word> getWordsByWordHash(Pagination page,@Param("hashcode")String hashcode);
}

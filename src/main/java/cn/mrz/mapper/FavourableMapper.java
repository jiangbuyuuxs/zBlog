package cn.mrz.mapper;

import cn.mrz.pojo.Favourable;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/6/12.
 */
@Repository
public interface FavourableMapper extends BaseMapper<Favourable> {
    int insertFavourable(Favourable favourable);
    Favourable findFavourableByItemId(String itemId);

    int insertFavourableList(List<Favourable> favourableBatch);
}

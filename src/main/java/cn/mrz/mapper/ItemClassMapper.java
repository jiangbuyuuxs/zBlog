package cn.mrz.mapper;

import cn.mrz.pojo.ItemClass;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/6/20.
 */
@Repository
public interface ItemClassMapper extends BaseMapper<ItemClass> {
    List<String> selectAllClassTitle();

    void insertItemClassList(List<ItemClass> itemClassBatch);

    /**
     * 包含level和parentId
     * @param itemClassBatch
     */
    void insertItemClassList2(List<ItemClass> itemClassBatch);

    List<ItemClass> selectNoRelationClassList();

    List<ItemClass> selectTopClassList();

    List<ItemClass> selectByParentId(Long parentId);

    int updateParentIdById(@Param("parentId")Long parentId, @Param("id")Long id);

    int removeParentIdById(Long id);


}

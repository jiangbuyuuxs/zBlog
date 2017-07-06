package cn.mrz.task;

import cn.mrz.service.BuyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/7/6.
 */
@Component
public class ItemTask {

    Logger logger = LoggerFactory.getLogger(ItemTask.class);

    @Autowired
    BuyService buyService;

    public void clearObsoleteItemTask(){
        logger.info("开始清理过时的商品数据");
        int clearObsoleteItemNum = buyService.clearObsoleteItem();
        logger.info("清理了{}条旧数据",clearObsoleteItemNum);
    }

}

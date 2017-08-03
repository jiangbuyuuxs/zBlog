package cn.mrz.mq.consumer;

import cn.mrz.service.BuyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/8/1.
 */
public class HandlerExcelConsumer implements MessageListener {

    Logger logger = LoggerFactory.getLogger(HandlerExcelConsumer.class);

    @Autowired
    BuyService buyService;

    @Override
    public void onMessage(Message message) {
        byte[] messageBody = message.getBody();
        String fileName = new String(messageBody);
        logger.info("开始处理{}",fileName);
        try {
            buyService.parseBuyFile(fileName);
            logger.info("处理{} 完毕",fileName);
        }catch (Exception e){

        }
    }
}

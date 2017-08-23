package cn.mrz.mq.consumer;

import cn.mrz.service.BuyService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/8/1.
 */
public class HandlerExcelConsumer implements ChannelAwareMessageListener {

    Logger logger = LoggerFactory.getLogger(HandlerExcelConsumer.class);

    @Autowired
    BuyService buyService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        byte[] messageBody = message.getBody();
        String fileName = new String(messageBody,"utf-8");
        logger.info("开始处理{}",fileName);
        try {
            boolean parseBuyFile = buyService.parseBuyFile(fileName);
            if(parseBuyFile){
                logger.info("处理{} 完毕",fileName);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }else{
                channel.basicNack(0, false, true);
                logger.error("处理{} 失败...重新发送消息",fileName );
            }
        }catch (Exception e){
            channel.basicNack(0, false, true);
            logger.error("处理{} 失败...重新发送消息...异常:{}", fileName,e.getLocalizedMessage());
        }
    }
}

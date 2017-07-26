package cn.mrz.mq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/7/26.
 */
@Service
public class MessageProducer {
    private Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Resource
    private AmqpTemplate amqpTemplate;

    public void sendSplitWordMessage(Object message){
        logger.info("发送博客分词消息");
        amqpTemplate.convertAndSend("splitWordQueueKey",message);
    }
}

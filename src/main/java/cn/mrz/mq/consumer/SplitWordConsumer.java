package cn.mrz.mq.consumer;

import cn.mrz.pojo.Blog;
import cn.mrz.service.BlogService;
import cn.mrz.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Administrator on 2017/7/26.
 */
@Component
public class SplitWordConsumer implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(SplitWordConsumer.class);

    @Autowired
    WordService wordService;
    @Autowired
    BlogService blogService;

    @Override
    public void onMessage(Message message) {
        byte[] messageBody = message.getBody();
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(messageBody));
            Long id = (Long) ois.readObject();
            logger.info("收到消息,开始处理id为{}的博文分词~~~", id);
            Blog blog = blogService.getById(id);
            if(blog==null){
                return;
            }
            wordService.getBlogWords(blog);
            logger.info("处理id为{}的博文分词结束~~~", id);
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
        }
    }
}

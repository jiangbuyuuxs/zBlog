package cn.mrz.mq.consumer;

import cn.mrz.pojo.Blog;
import cn.mrz.service.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Administrator on 2017/7/26.
 */
public class SplitWordConsumer implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(SplitWordConsumer.class);

    @Autowired
    WordService wordService;

    @Override
    public void onMessage(Message message) {
        byte[] messageBody = message.getBody();
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(messageBody));
            Blog blog = (Blog) ois.readObject();
            wordService.getBlogWords(blog);

            logger.info("收到消息,开始处理id为{}的博文分词~~~", blog.getId());

        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }
    }
}

package com.miaoshaproject.rabbitmq;

import com.miaoshaproject.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
    public static final String QUEUE = "queue";
    @Autowired
    AmqpTemplate amqpTemplate;
    public void send(Object message){
        String msg = RedisService.beanToString(message);
        log.info("sendMessage:"+message);
        amqpTemplate.convertAndSend(QUEUE,msg);
    }
}

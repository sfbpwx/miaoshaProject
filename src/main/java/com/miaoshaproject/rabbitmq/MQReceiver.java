package com.miaoshaproject.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
    @RabbitListener(queues=MQSender.QUEUE)
    public void receive (String message){
        log.info("receiveMessage:"+message);
    }
}

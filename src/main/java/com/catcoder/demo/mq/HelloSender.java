package com.catcoder.demo.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * mq发送者
 */
public class HelloSender {
    @Autowired
    private AmqpTemplate amqpTemplate;
    public void send(){
        String content = "hello"+new Date();
        System.out.println("Sender: "+ content);

        amqpTemplate.convertAndSend("hello", content);
    }
}

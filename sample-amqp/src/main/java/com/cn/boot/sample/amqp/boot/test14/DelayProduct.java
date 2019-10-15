package com.cn.boot.sample.amqp.boot.test14;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Slf4j
//@Component
public class DelayProduct {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send() {
        String exchange = "test14.delay.exchange";
        String routingKey = "test14";
        String body = "Hello, delay message!";

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("x-delay", 5000);
        Message message = new Message(body.getBytes(), messageProperties);

        log.info("发送延时消息-开始");
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        log.info("发送延时消息-完成");
    }
}

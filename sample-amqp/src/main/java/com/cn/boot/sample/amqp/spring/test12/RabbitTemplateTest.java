package com.cn.boot.sample.amqp.spring.test12;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * RabbitTemplate发送消息模板
 *
 * @author Chen Nan
 * @date 2019/6/9.
 */
@Slf4j
public class RabbitTemplateTest {
    private String exchangeName = "test12.direct.exchange";

    private RabbitTemplate rabbitTemplate;

    public RabbitTemplateTest(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        log.info("开始发送消息");
        sendMessage1();
        sendMessage2();
        log.info("成功发送消息");
    }

    private void sendMessage1() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentEncoding("UTF-8");
        messageProperties.getHeaders().put("desc", "消息描述");

        String body = "Hello RabbitTemplate!";
        Message message = new Message(body.getBytes(), messageProperties);

        // MessagePostProcessor可用于发送消息后置处理
        rabbitTemplate.convertAndSend(exchangeName, "test12.q1", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                log.info("添加额外的配置");
                message.getMessageProperties().getHeaders().put("desc", "修改后的消息描述");
                return message;
            }
        });
    }

    private void sendMessage2() {
        String body = "sendMessage2";
        // 第一种方式，接收Message对象
        MessageProperties messageProperties = new MessageProperties();
        Message message = new Message(body.getBytes(), messageProperties);
        rabbitTemplate.send(exchangeName, "test12.q2", message);

        // 第二种方式，接收Object对象
        rabbitTemplate.convertAndSend(exchangeName, "test12.q1", body);
    }
}

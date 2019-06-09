package com.cn.boot.sample.amqp.original.message.test5;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 消息属性-生产者
 *
 * @author Chen Nan
 * @date 2019/6/2.
 */
//@Component
@Slf4j
public class MessagePropertyProduct {
    static {
        try {
            init();
        } catch (IOException | TimeoutException e) {
            log.error("error:", e);
        }
    }

    private static void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        Map<String, Object> headers = new HashMap<>();
        headers.put("key1", "val1");
        headers.put("key2", 222);

        AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8") // 字符集
                .contentType("json")
                .expiration("10000") // 过期时间，过期清除
                .headers(headers) // 自定义属性
                .build();

        log.info("开始发送");
        String routingKey = "test05";
        String content = "Hello RabbitMQ!";
        channel.basicPublish("", routingKey, properties, content.getBytes());
        log.info("发送成功");

        channel.close();
        connection.close();
    }
}

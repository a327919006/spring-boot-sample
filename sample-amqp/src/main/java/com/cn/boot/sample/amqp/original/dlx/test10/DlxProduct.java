package com.cn.boot.sample.amqp.original.dlx.test10;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Dlx-生产者
 *
 * @author Chen Nan
 * @date 2019/6/9.
 */
//@Component
@Slf4j
public class DlxProduct {
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

        log.info("开始发送");
        String exchange = "test10_dlx_exchange";
        String routingKey = "test10";

        String content = "Hello RabbitMQ! DLX!";

        AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8") // 字符集
                .expiration("10000") // 过期时间，过期清除
                .build();

        channel.basicPublish(exchange, routingKey, properties, content.getBytes());
        log.info("发送成功");
    }
}

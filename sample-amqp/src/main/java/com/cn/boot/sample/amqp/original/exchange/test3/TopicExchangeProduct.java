package com.cn.boot.sample.amqp.original.exchange.test3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * TopicExchange-生产者
 *
 * @author Chen Nan
 * @date 2019/6/2.
 */
//@Component
@Slf4j
public class TopicExchangeProduct {
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
        String exchangeName = "test03_topic_exchange";
        String routingKey1 = "test03.add";
        String routingKey2 = "test03.update";
        String routingKey3 = "test03.delete.abc";
        String content = "Hello RabbitMQ! Topic Exchange...";
        channel.basicPublish(exchangeName, routingKey1, null, content.getBytes());
        channel.basicPublish(exchangeName, routingKey2, null, content.getBytes());
        channel.basicPublish(exchangeName, routingKey3, null, content.getBytes());
        log.info("发送成功");

//        channel.close();
//        connection.close();
    }
}

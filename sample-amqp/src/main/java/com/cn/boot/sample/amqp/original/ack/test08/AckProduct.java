package com.cn.boot.sample.amqp.original.ack.test08;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Ack-生产者
 *
 * @author Chen Nan
 * @date 2019/6/2.
 */
//@Component
@Slf4j
public class AckProduct {
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
        String exchange = "test08_ack_exchange";
        String routingKey = "test08";

        for (int i = 0; i < 5; i++) {
            String content = "Hello RabbitMQ! ACK!" + i;

            Map<String, Object> headers = new HashMap<>();
            headers.put("num", i);

            AMQP.BasicProperties properties = new AMQP.BasicProperties()
                    .builder()
                    .deliveryMode(2)
                    .contentEncoding(StandardCharsets.UTF_8.displayName())
                    .headers(headers)
                    .build();

            channel.basicPublish(exchange, routingKey, properties, content.getBytes());
        }
        log.info("发送成功");

        channel.close();
        connection.close();
    }
}

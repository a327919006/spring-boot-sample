package com.cn.boot.sample.amqp.original.message.test5;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * 消息属性-消费者
 *
 * @author Chen Nan
 * @date 2019/6/2.
 */
//@Component
@Slf4j
public class MessagePropertyConsumer {

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

        String queue = "test05";
        channel.queueDeclare(queue, true, false, false, null);

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                String routingKey = envelope.getRoutingKey();
                String contentType = properties.getContentType();
                Map<String, Object> headers = properties.getHeaders();

                long deliveryTag = envelope.getDeliveryTag();
                log.info("收到消息：message = " + message);
                log.info("routingKey = " + routingKey);
                log.info("contentType = " + contentType);
                log.info("deliveryTag = " + deliveryTag);


                Set<String> keySet = headers.keySet();
                keySet.forEach((key) -> log.info("key={},val={}", key, headers.get(key)));
            }
        };

        channel.basicConsume(queue, true, consumer);
    }
}

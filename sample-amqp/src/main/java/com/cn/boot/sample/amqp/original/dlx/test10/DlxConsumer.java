package com.cn.boot.sample.amqp.original.dlx.test10;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Dlx-消费者
 * 死信队列
 *
 * @author Chen Nan
 * @date 2019/6/9.
 */
//@Component
@Slf4j
public class DlxConsumer {

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

        String exchangeName = "test10_dlx_exchange";
        String queueName = "test10_dlx_queue";
        String routingKey = "test10";

        String dlxExchangeName = "dlx.exchange";
        String dlxQueueName = "dlx.queue";
        String dlxRoutingKey = "#";


        // 设置死信队列
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", dlxExchangeName);

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, null);
        channel.queueDeclare(queueName, true, false, false, arguments);
        channel.queueBind(queueName, exchangeName, routingKey);

        // 声明死信队列
        channel.exchangeDeclare(dlxExchangeName, BuiltinExchangeType.TOPIC, true, false, false, null);
        channel.queueDeclare(dlxQueueName, true, false, false, null);
        channel.queueBind(dlxQueueName, dlxExchangeName, dlxRoutingKey);


        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                log.info("-------------收到消息------------");
                log.info("message = " + message);
                log.info("consumerTag = " + consumerTag);
                log.info("envelope = " + envelope);
                log.info("properties = " + properties);
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }
}

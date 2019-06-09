package com.cn.boot.sample.amqp.original.ack.test08;

import cn.hutool.core.thread.ThreadUtil;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * ACK-消费者
 * ACK与NACK
 *
 * @author Chen Nan
 * @date 2019/6/2.
 */
//@Component
@Slf4j
public class AckConsumer {

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

        String exchangeName = "test08_ack_exchange";
        String queueName = "test08_ack_queue";
        String routingKey = "test08";

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, null);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                long deliveryTag = envelope.getDeliveryTag();
                log.info("-------------收到消息------------");
                log.info("message = " + message);
                log.info("consumerTag = " + consumerTag);
                log.info("envelope = " + envelope);
                log.info("properties = " + properties);

                int num = (int) properties.getHeaders().get("num");
                if (num == 0) {
                    // 消息重回队列，回到队列结尾
                    channel.basicNack(deliveryTag, false, true);
                } else {
                    // 成功消费消息
                    channel.basicAck(deliveryTag, false);
                }

                ThreadUtil.sleep(1000);

            }
        };

        // 自动ACK设为false
        channel.basicConsume(queueName, false, consumer);
    }
}

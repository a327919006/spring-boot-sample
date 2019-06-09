package com.cn.boot.sample.amqp.original.returnlistener.test6;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * ReturnListener-生产者
 * 监听无法被路由到的消息
 *
 * @author Chen Nan
 * @date 2019/6/2.
 */
//@Component
@Slf4j
public class ReturnListenerProduct {
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

        // 添加监听器
        channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> {
            log.info("---------------ReturnListener---------------");
            log.info("replyCode = " + replyCode);
            log.info("replyText = " + replyText);
            log.info("exchange = " + exchange);
            log.info("routingKey = " + routingKey);
            log.info("properties = " + properties);
            log.info("body = " + new String(body, StandardCharsets.UTF_8));
            log.info("---------------ReturnListener-end---------------");
        });

        log.info("开始发送");
        String exchange = "test06_return_exchange";
        String routingKey1 = "return.key";
        String routingKey2 = "error.key";
        String content = "Hello RabbitMQ! Return Listener!";

        // 这里mandatory要设置为true，否则不可达消息broker会自动删除
        channel.basicPublish(exchange, routingKey1, true, null, content.getBytes());
        channel.basicPublish(exchange, routingKey2, true, null, content.getBytes());
        log.info("发送成功");
    }
}

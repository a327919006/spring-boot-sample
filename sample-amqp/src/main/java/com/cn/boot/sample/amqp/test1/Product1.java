package com.cn.boot.sample.amqp.test1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2019/6/2.
 */
@Component
public class Product1 {

    public void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        String content = "Hello RabbitMQ!";
        channel.basicPublish("", "test001", null, content.getBytes());

        channel.close();
        connection.close();
    }
}

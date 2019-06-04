package com.cn.boot.sample.amqp.test1;

import com.rabbitmq.client.BuiltinExchangeType;
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
public class Consumer1 {

    public void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("/");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare("test001", BuiltinExchangeType.DIRECT, true, false, null);

        channel.close();
        connection.close();
    }
}

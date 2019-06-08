package com.cn.boot.sample.amqp.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 自定义消息消费者
 *
 * @author Chen Nan
 * @date 2019/6/8.
 */
@Slf4j
public class TestConsumer extends DefaultConsumer {
    private Channel channel;

    public TestConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, StandardCharsets.UTF_8);
        long deliveryTag = envelope.getDeliveryTag();
        log.info("收到消息：message = " + message);
        log.info("consumerTag = " + consumerTag);
        log.info("envelope = " + envelope);
        log.info("properties = " + properties);

        channel.basicAck(deliveryTag, false);
    }
}

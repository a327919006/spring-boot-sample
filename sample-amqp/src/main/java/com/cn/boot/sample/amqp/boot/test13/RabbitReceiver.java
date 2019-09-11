package com.cn.boot.sample.amqp.boot.test13;

import com.cn.boot.sample.api.model.dto.client.ClientAddReq;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2019/6/10.
 */
//@Component
@Slf4j
public class RabbitReceiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.test13.queue.name}"),
            exchange = @Exchange(value = "${spring.rabbitmq.listener.test13.exchange.name}", type = "${spring.rabbitmq.listener.test13.exchange.type}"),
            key = "${spring.rabbitmq.listener.test13.key}"))
    @RabbitHandler
    public void onMessage(String data, Channel channel, Message message) throws IOException {
        log.info("-----onMessage------");
        log.info("data = " + data);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        String error = "error";
        if (data.contains(error)) {
            channel.basicNack(deliveryTag, false, false);
        } else {
            channel.basicAck(deliveryTag, false);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.test13.queue.name}"),
            exchange = @Exchange(value = "${spring.rabbitmq.listener.test13.exchange.name}", type = "${spring.rabbitmq.listener.test13.exchange.type}"),
            key = "${spring.rabbitmq.listener.test13.key}"))
    @RabbitHandler
    public void onMessage(@Payload ClientAddReq req, Channel channel, Message message) throws IOException {
        log.info("-----onMessage------");
        log.info("data = " + req);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        channel.basicNack(deliveryTag, false, false);
    }
}

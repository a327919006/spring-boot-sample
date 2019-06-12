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
@Component
@Slf4j
public class RabbitReceiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "test13_queue"),
            exchange = @Exchange(value = "test13_exchange", type = "topic"),
            key = "test13.#"))
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
            value = @Queue(value = "test13_queue"),
            exchange = @Exchange(value = "test13_exchange", type = "topic"),
            key = "test13.#"))
    @RabbitHandler
    public void onMessage(@Payload ClientAddReq req, Channel channel, Message message) throws IOException {
        log.info("-----onMessage------");
        log.info("data = " + req);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        channel.basicNack(deliveryTag, false, false);
    }
}

package com.cn.boot.sample.amqp.boot.test13;

import cn.hutool.core.util.IdUtil;
import com.cn.boot.sample.api.model.dto.client.ClientAddReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2019/6/10.
 */
//@Component
@Slf4j
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            log.info("-----confirm------");
            String id = correlationData.getId();
            log.info("id = " + id);
            log.info("ack = " + ack);
            log.info("cause = " + cause);
        }
    };

    private final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            log.info("-----returnedMessage------");
            log.info("message = " + new String(message.getBody()));
            log.info("replyText = " + replyText);
            log.info("exchange = " + exchange);
            log.info("routingKey = " + routingKey);
        }
    };

    public void sendMessage(String data) {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(IdUtil.simpleUUID());

        String returnMsg = "return";
        if (data.contains(returnMsg)) {
            rabbitTemplate.convertAndSend("test13_exchange", "testaa", data, correlationData);
        } else {
            rabbitTemplate.convertAndSend("test13_exchange", "test13", data, correlationData);
        }
    }

    public void sendMessage(ClientAddReq req) {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(IdUtil.simpleUUID());

        rabbitTemplate.convertAndSend("test13_exchange", "test13", req, correlationData);
    }
}

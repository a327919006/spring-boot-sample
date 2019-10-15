package com.cn.boot.sample.amqp.prod;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Component
@Slf4j
public class ProdTestProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendData(String data) {
        rabbitTemplate.convertAndSend(ProdRabbitMqConfig.PROD_TEST_EXCHANGE, ProdRabbitMqConfig.PROD_TEST_KEY, data);
    }
}

package com.cn.boot.sample.amqp.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
//@Configuration
public class RabbitMqDelayConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public Exchange delayExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new ExchangeBuilder("test14.delay.exchange", "x-delayed-message").durable(true).withArguments(arguments).build();
    }

    @Bean
    public Queue delayQueue() {
        return new Queue("test14.delay.queue");
    }

    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue())
                .to(delayExchange())
                .with("test14").noargs();
    }
}

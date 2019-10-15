package com.cn.boot.sample.amqp.prod;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Configuration
public class ProdRabbitMqConfig {
    public static final String PROD_TEST_EXCHANGE = "prod_test_exchange";
    public static final String PROD_TEST_KEY = "prod_test_key";
    public static final String PROD_TEST_QUEUE = "prod_test_queue";

    @Bean(PROD_TEST_EXCHANGE)
    public Exchange getCabinetLogExchange() {
        return ExchangeBuilder
                .directExchange(PROD_TEST_EXCHANGE)
                .durable(true)
                .build()
                ;
    }

    @Bean(PROD_TEST_QUEUE)
    public Queue getCabinetLogQueue() {
        return QueueBuilder
                .durable(PROD_TEST_QUEUE)
                .build()
                ;
    }

    @Bean
    public Binding getCabinetLogBinding(Queue queue, Exchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(PROD_TEST_KEY)
                .noargs()
                ;
    }
}

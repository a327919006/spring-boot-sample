package com.cn.boot.sample.amqp.spring.test11;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

/**
 * RabbitAdmin，用于声明交换机、队列、绑定关系
 *
 * @author Chen Nan
 * @date 2019/6/9.
 */
//@Component
@Slf4j
public class RabbitAdminTest {

    private RabbitAdmin rabbitAdmin;

    public RabbitAdminTest(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
        testRabbitAdmin();
    }

    private void testRabbitAdmin() {
        log.info("【RabbitAdminTest】开始");
        // ---声明Exchange---
        String directExchangeName = "test11.direct.exchange";
        String topicExchangeName = "test11.topic.exchange";
        String fanoutExchangeName = "test11.fanout.exchange";
        DirectExchange directExchange = new DirectExchange(directExchangeName);
        TopicExchange topicExchange = new TopicExchange(topicExchangeName);
        FanoutExchange fanoutExchange = new FanoutExchange(fanoutExchangeName);
        rabbitAdmin.declareExchange(directExchange);
        rabbitAdmin.declareExchange(topicExchange);
        rabbitAdmin.declareExchange(fanoutExchange);

        // ---声明Queue---
        String directQueueName = "test11.direct.queue";
        String topicQueueName = "test11.topic.queue";
        String fanoutQueueName = "test11.fanout.queue";
        Queue directQueue = new Queue(directQueueName);
        Queue topicQueue = new Queue(topicQueueName);
        Queue fanoutQueue = new Queue(fanoutQueueName);
        rabbitAdmin.declareQueue(directQueue);
        rabbitAdmin.declareQueue(topicQueue);
        rabbitAdmin.declareQueue(fanoutQueue);

        // ---声明绑定关系---
        String directRoutingKey = "test11";
        String topicRoutingKey = "test11.#";
        rabbitAdmin.declareBinding(new Binding(directQueueName, Binding.DestinationType.QUEUE, directExchangeName, directRoutingKey, null));
        // 如果queue和exchange没声明，会自动声明
        rabbitAdmin.declareBinding(BindingBuilder.bind(topicQueue).to(topicExchange).with(topicRoutingKey));
        rabbitAdmin.declareBinding(BindingBuilder.bind(fanoutQueue).to(fanoutExchange));

        // 清空队列消息
        rabbitAdmin.purgeQueue("dlx.queue");
        log.info("【RabbitAdminTest】完成");
    }
}

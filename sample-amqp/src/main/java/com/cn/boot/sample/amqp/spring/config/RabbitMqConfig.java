package com.cn.boot.sample.amqp.spring.config;

import com.cn.boot.sample.amqp.spring.converter.TextMessageConverter;
import com.cn.boot.sample.amqp.spring.test12.RabbitTemplateTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2019/6/9.
 */
//@Configuration
@Slf4j
public class RabbitMqConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setAddresses("127.0.0.1:5672");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public RabbitTemplateTest rabbitTemplateTest(RabbitTemplate rabbitTemplate) {
        return new RabbitTemplateTest(rabbitTemplate);
    }

    @Bean
    public DirectExchange test12DirectExchange() {
        return new DirectExchange("test12.direct.exchange");
    }

    @Bean
    public Queue test12DirectQueue1() {
        return new Queue("test12.direct.queue1");
    }

    @Bean
    public Queue test12DirectQueue2() {
        return new Queue("test12.direct.queue2");
    }

    @Bean
    public Binding test12Binding1() {
        return BindingBuilder.bind(test12DirectQueue1())
                .to(test12DirectExchange())
                .with("test12.q1");
    }

    @Bean
    public Binding test12Binding2() {
        return BindingBuilder.bind(test12DirectQueue2())
                .to(test12DirectExchange())
                .with("test12.q2");
    }

    @Bean
    SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(test12DirectQueue1(), test12DirectQueue2());
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        container.setDefaultRequeueRejected(false);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);

        // 方式一 设置MessageListener
//        container.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                String body = new String(message.getBody());
//                log.info("收到消息, body={}", body);
//            }
//        });


        // 方式二 使用MessageListenerAdapter
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        // 可自定义调用的方法名，默认是handleMessage
        adapter.setDefaultListenerMethod("handleMessage");
        // 可设置指定队列使用指定方法进行处理
        Map<String, String> queueMethodMap = new HashMap<>(16);
        queueMethodMap.put("test12.direct.queue1", "handleDirect1");
        queueMethodMap.put("test12.direct.queue2", "handleDirect2");
        adapter.setQueueOrTagToMethodName(queueMethodMap);
        // 可设置自定义消息转换器
        adapter.setMessageConverter(new TextMessageConverter());

        container.setMessageListener(adapter);
        return container;
    }
}

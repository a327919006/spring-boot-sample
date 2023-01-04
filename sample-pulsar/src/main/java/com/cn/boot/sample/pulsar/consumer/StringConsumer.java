package com.cn.boot.sample.pulsar.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class StringConsumer {

    @Value("${pulsar.consumer.topic}")
    private String topic;
    @Value("${pulsar.consumer.subscription}")
    private String subscription;

    @Autowired
    private PulsarClient client;
    private Consumer consumer = null;

    @PostConstruct
    public void initConsumer() throws Exception {
        try {
            //创建consumer
            consumer = client.newConsumer()
                    .topic(topic.split(","))
                    .subscriptionName(subscription)
                    .subscriptionType(SubscriptionType.Exclusive)//指定消费模式，包含：Exclusive，Failover，Shared，Key_Shared。默认Exclusive模式
                    .subscriptionInitialPosition(SubscriptionInitialPosition.Latest)//指定从哪里开始消费还有Latest，valueof可选，默认Latest
                    .negativeAckRedeliveryDelay(60, TimeUnit.SECONDS)//指定消费失败后延迟多久broker重新发送消息给consumer，默认60s
                    .subscribe();

            // 开始消费
            new Thread(() -> {
                try {
                    start();
                } catch (Exception e) {
                    log.error("消费Pulsar数据异常，停止Pulsar连接：", e);
                    close();
                }
            }).start();

        } catch (Exception e) {
            log.error("Pulsar初始化异常：", e);
            throw e;
        }
    }

    private void start() throws Exception {
        //消费消息
        while (true) {
            Message message = consumer.receive();
            String key = message.getKey();
            String msg = new String(message.getData());

            if (StringUtils.isNotEmpty(msg)) {
                try {
                    handle(key, msg);
                    consumer.acknowledge(message);
                } catch (Exception e) {
                    log.error("消费Pulsar数据异常，key【{}】，msg【{}】：", key, msg, e);
                    consumer.negativeAcknowledge(message);
                }
            }
        }
    }

    /**
     * 线程池异步处理Pulsar推送的数据
     */
//    @Async("threadPoolTaskExecutor")
    public void handle(String key, String msg) {
        log.info("收到数据 key={} msg={}", key, msg);
    }


    public void close() {
        try {
            consumer.close();
        } catch (PulsarClientException e) {
            log.error("关闭Pulsar消费者失败：", e);
        }
        try {
            client.close();
        } catch (PulsarClientException e) {
            log.error("关闭Pulsar连接失败：", e);
        }
    }
}

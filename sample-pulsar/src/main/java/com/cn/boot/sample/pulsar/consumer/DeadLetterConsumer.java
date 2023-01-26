package com.cn.boot.sample.pulsar.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 死信队列消费者
 *
 * @author Chen Nan
 */
@Slf4j
@Component
public class DeadLetterConsumer {


    @Autowired
    private PulsarClient client;
    private Consumer consumer = null;

    @PostConstruct
    public void initConsumer() throws Exception {
        try {
            //创建consumer
            consumer = client.newConsumer()
                    .topic("test-DLQ")
                    .subscriptionName("test-DLQ-group")
                    .messageListener(new MessageListener<byte[]>() {
                        @Override
                        public void received(Consumer<byte[]> consumer, Message<byte[]> msg) {
                            handle(msg);
                        }
                    })
                    .subscribe();
        } catch (Exception e) {
            log.error("【DeadLetterConsumer】Pulsar初始化异常：", e);
            throw e;
        }
    }

    public void handle(Message message) {
        String key = message.getKey();
        String msg = new String(message.getData());

        if (StringUtils.isNotEmpty(msg)) {
            try {
                String producerName = message.getProducerName();
                long eventTime = message.getEventTime();
                long publishTime = message.getPublishTime();
                String topicName = message.getTopicName();
                Map properties = message.getProperties();
                log.info("【DeadLetterConsumer】收到数据 key={} " +
                                "msg={} " +
                                "producerName={} " +
                                "eventTime={} " +
                                "publishTime={} " +
                                "topicName={} ",
                        key, msg, producerName, eventTime, publishTime, topicName);
                for (Object propertyKey : properties.keySet()) {
                    log.info("【DeadLetterConsumer】propertyKey={} value={}", propertyKey, properties.get(propertyKey));
                }
                consumer.acknowledge(message);
            } catch (Exception e) {
                log.error("【DeadLetterConsumer】消费Pulsar数据异常，key【{}】，msg【{}】：", key, msg, e);
                consumer.negativeAcknowledge(message);
            }
        }
    }
}

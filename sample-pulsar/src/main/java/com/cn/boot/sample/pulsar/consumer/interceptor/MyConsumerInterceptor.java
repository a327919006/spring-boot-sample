package com.cn.boot.sample.pulsar.consumer.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.ConsumerInterceptor;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;

import java.util.Set;

/**
 * @author Chen Nan
 */
@Slf4j
public class MyConsumerInterceptor implements ConsumerInterceptor {

    /**
     * 在receive() or receiveAsync()前触发.可以修改消息内容
     */
    @Override
    public Message beforeConsume(Consumer consumer, Message message) {
        log.info("beforeConsume");
        return message;
    }

    /**
     * 在consumer发送消息ack到broker前
     */
    @Override
    public void onAcknowledge(Consumer consumer, MessageId messageId, Throwable exception) {
        log.info("onAcknowledge");
    }

    /**
     * 在consumer发送消息累计ack到broker前
     */
    @Override
    public void onAcknowledgeCumulative(Consumer consumer, MessageId messageId, Throwable exception) {
        log.info("onAcknowledgeCumulative");
    }

    /**
     * 在发送NegativeAck到broker前
     */
    @Override
    public void onNegativeAcksSend(Consumer consumer, Set set) {
        log.info("onNegativeAcksSend");
    }

    /**
     * 在发送ack超时时
     */
    @Override
    public void onAckTimeoutSend(Consumer consumer, Set set) {
        log.info("onAckTimeoutSend");
    }

    /**
     * 在分区数变化时
     */
    @Override
    public void onPartitionsChange(String topicName, int partitions) {
        log.info("onPartitionsChange");
    }

    @Override
    public void close() {
        log.info("close");
    }
}

package com.cn.boot.sample.pulsar.consumer.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.interceptor.ProducerInterceptor;

/**
 * @author Chen Nan
 */
@Slf4j
public class MyProducerInterceptor implements ProducerInterceptor {

    /**
     * 确认当前消息是否需要拦截
     */
    @Override
    public boolean eligible(Message message) {
        log.info("eligible");
        return true;
    }

    /**
     * 在发送消息到broker前触发，可以修改消息内容
     */
    @Override
    public Message beforeSend(Producer producer, Message message) {
        log.info("beforeSend");
        return message;
    }

    /**
     * 在收到broker消息发送结果时触发，消息发送可能成功或失败
     */
    @Override
    public void onSendAcknowledgement(Producer producer, Message message, MessageId msgId, Throwable exception) {
        log.info("onSendAcknowledgement");
    }

    /**
     * 当分区数变化时触发
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

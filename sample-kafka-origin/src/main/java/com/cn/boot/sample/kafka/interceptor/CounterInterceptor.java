package com.cn.boot.sample.kafka.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;

import java.util.Map;

/**
 * 自定义拦截器，用于消息计数
 *
 * @author Chen Nan
 */
@Slf4j
public class CounterInterceptor implements ProducerInterceptor<String, String> {
    private long success;
    private long error;

    @Override
    public void configure(Map<String, ?> configs) {

    }

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (metadata != null) {
            success++;
        } else {
            error++;
        }
    }

    @Override
    public void close() {
        log.info("success={}, error={}", success, error);
    }


}

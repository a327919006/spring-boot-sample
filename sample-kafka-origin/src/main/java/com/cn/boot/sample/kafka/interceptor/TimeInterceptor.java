package com.cn.boot.sample.kafka.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;

import java.util.Map;

/**
 * 自定义拦截器，拼接时间戳到每条数据value中
 *
 * @author Chen Nan
 */
@Slf4j
public class TimeInterceptor implements ProducerInterceptor<String, String> {

    @Override
    public void configure(Map<String, ?> configs) {

    }

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        String topic = record.topic();
        String key = record.key();
        Integer partition = record.partition();
        Long timestamp = record.timestamp();
        Headers headers = record.headers();
        String value = System.currentTimeMillis() + "," + record.value();
        return new ProducerRecord<>(topic, partition, timestamp, key, value, headers);
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }


}

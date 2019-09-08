package com.cn.boot.sample.kafka.consumer;

import com.cn.boot.sample.kafka.constant.KafkaConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class TestConsumer {

    @KafkaListener(topics = KafkaConstant.TOPIC_TEST)
    public void listen(ConsumerRecord<?, String> record) {
        String value = record.value();
        log.info("value = {}", value);
    }
}

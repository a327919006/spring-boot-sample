package com.cn.boot.sample.kafka.avro.consumer;

import com.cn.boot.sample.kafka.avro.model.UserAvro;
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

    @KafkaListener(topics = "testTopic")
    public void listen(ConsumerRecord<?, UserAvro> record) {
        UserAvro value = record.value();
        log.info("value = {}", value);
    }
}

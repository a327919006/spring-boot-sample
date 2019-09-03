package com.cn.boot.sample.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class TestProducer {

    private KafkaProducer<String, String> kafkaProducer;

    public TestProducer() {
        Properties p = new Properties();
        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");//kafka地址，多个地址用逗号分割
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kafkaProducer = new KafkaProducer<>(p);
    }

    public void send(String topic, String msg) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, msg);
        kafkaProducer.send(record, (recordMetadata, e) -> {
            log.info("【Kafka】发送消息回调，offset:{},partition:{}", recordMetadata.offset(), recordMetadata.partition());
            if (e != null) {
                log.error("【Kafka】发送异常:" + e.getMessage(), e);
            }
        });
    }
}

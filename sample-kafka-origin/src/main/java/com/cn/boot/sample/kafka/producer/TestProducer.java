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
        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-broker-0.mydomain.com:443");//kafka地址，多个地址用逗号分割
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 仅SASL
//        p.put("security.protocol", "SASL_PLAINTEXT");
        // 仅SSL
//        p.put("security.protocol", "SSL");
        // SASL+SSL
//        p.put("security.protocol", "SASL_SSL");
        // 根据SASL密码的方式如：PLAIN、SCRAM-SHA-256、SCRAM-SHA-512
//        p.put("sasl.mechanism", "SCRAM-SHA-512");
        // SSL证书
//        p.put("ssl.truststore.location", "E:/truststore.jks");
        // SSL证书密码
//        p.put("ssl.truststore.password", "dbEiQpnJB7Of");
        // 用于SASL，指定账号密码的配置文件
//        System.setProperty("java.security.auth.login.config", "E:\\software\\kafka_2.13-2.8.0\\config\\kafka_client_jaas_scram.conf");


        kafkaProducer = new KafkaProducer<>(p);
        System.out.println(kafkaProducer);
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

package com.cn.boot.sample.kafka.producer;

import com.cn.boot.sample.kafka.interceptor.CounterInterceptor;
import com.cn.boot.sample.kafka.interceptor.TimeInterceptor;
import com.cn.boot.sample.kafka.partitioner.TestPartitioner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
        // 指定自定义分区器，不指定则为DefaultPartitioner
        p.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, TestPartitioner.class);
        // 添加自定义拦截器，默认为空
        p.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, Lists.newArrayList(TimeInterceptor.class, CounterInterceptor.class));
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

    /**
     * 异步发送
     */
    public void send(String topic, String msg) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, msg);
        kafkaProducer.send(record, (recordMetadata, e) -> {
            log.info("【Kafka】发送消息回调，offset:{},partition:{}", recordMetadata.offset(), recordMetadata.partition());
            if (e != null) {
                log.error("【Kafka】发送异常:" + e.getMessage(), e);
            }
        });
    }

    /**
     * 同步发送
     */
    public void sendSync(String topic, String msg) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, msg);
        Future<RecordMetadata> result = kafkaProducer.send(record);
        try {
            RecordMetadata recordMetadata = result.get();
            log.info("【Kafka】同步发送消息，offset:{},partition:{}", recordMetadata.offset(), recordMetadata.partition());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

package com.cn.boot.sample.kafka.consumer;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.cn.boot.sample.kafka.constant.KafkaConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class TestConsumer {

    public TestConsumer() {
        Properties p = new Properties();
        p.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-broker-0.mydomain.com:443");
        p.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstant.GROUP_TEST);
        // 仅SASL
//        p.put("security.protocol", "SASL_PLAINTEXT");
        // 仅SSL
//        p.put("security.protocol", "SSL");
        // SASL+SSL
//        p.put("security.protocol", "SASL_SSL");
        // 用于SASL，根据创建账号时密码的加密方式：PLAIN、SCRAM-SHA-256、SCRAM-SHA-512
//        p.put("sasl.mechanism", "SCRAM-SHA-512");
        // SSL证书
//        p.put("ssl.truststore.location", "E:/truststore.jks");
        // SSL证书密码
//        p.put("ssl.truststore.password", "dbEiQpnJB7Of");
        // 用于SASL，指定账号密码的配置文件
//        System.setProperty("java.security.auth.login.config", "E:\\software\\kafka_2.13-2.8.0\\config\\kafka_client_jaas_scram.conf");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(p);
        kafkaConsumer.subscribe(Collections.singletonList(KafkaConstant.TOPIC_TEST));// 订阅消息

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().
                setNamePrefix("consumer-pool-")
                .build();
        ExecutorService executor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        executor.execute(() -> {
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    log.info("【Kafka】收到消息，topic:{},offset:{},消息:{}", record.topic(), record.offset(), record.value());
                }
            }
        });
    }
}

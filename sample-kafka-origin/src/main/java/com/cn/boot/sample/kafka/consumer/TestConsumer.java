package com.cn.boot.sample.kafka.consumer;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.cn.boot.sample.kafka.constant.KafkaConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
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
        p.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        p.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        p.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstant.GROUP_TEST);
        // 新消费者组从哪个位置开始消费，earliest：从最旧，latest：从最新(默认)
        p.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        // 自动提交消费者offset，默认true
        p.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // 自动提交频率，默认5000ms
        p.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
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
        // 订阅消息
        kafkaConsumer.subscribe(Collections.singletonList(KafkaConstant.TOPIC_TEST));

        // 订阅消息，同时监听Rebalance事件
//        kafkaConsumer.subscribe(Collections.singletonList(KafkaConstant.TOPIC_TEST),
//                new ConsumerRebalanceListener() {
//                    /**
//                     * Rebalance之前调用
//                     */
//                    @Override
//                    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
//
//                    }
//
//                    /**
//                     * Rebalance之后调用
//                     */
//                    @Override
//                    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
//
//                    }
//                });

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

                // 手动-同步提交offset，需先关闭自动提交，调用此方法后会阻塞直到提交完成
//                kafkaConsumer.commitSync();
                // 手动-异步提交offset，需先关闭自动提交，调用此方法后不会阻塞
//                kafkaConsumer.commitAsync((offsets, exception) -> {
//                    if (exception != null) {
//                        log.error("error={}", exception);
//                    }
//                });
            }
        });
    }
}

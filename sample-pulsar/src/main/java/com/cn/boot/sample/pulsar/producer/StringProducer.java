package com.cn.boot.sample.pulsar.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.MessageIdImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class StringProducer {

    @Value("${pulsar.producer.topic}")
    private String topic;

    @Autowired
    private PulsarClient client;
    private Producer<byte[]> producer = null;

    @PostConstruct
    public void initPulsar() throws Exception {
        //创建producer
        producer = client.newProducer()
                .producerName("StringProducer")
                .topic(topic)
                //访问模式
                // Shared 共享（默认）：多个生产者可以往同一个主题发送消息；
                // Exclusive 独占：只允许一个生产者往主题发送数据，其他生产者连接时返回错误；
                // WaitForExclusive 等待独占：只允许一个生产者往主题发送数据，其他生产者挂起（线程阻塞）；
                .accessMode(ProducerAccessMode.Shared)
                //设置发送超时(默认30s)；如果在sendTimeout过期之前服务器没有确认消息，则会发生错误。设置为0代表无限制，建议配置为0
                .sendTimeout(0, TimeUnit.SECONDS)
                //设置当消息队列中等待的消息已满时，Producer.send 和 Producer.sendAsync 是否应该block阻塞。默认为false，达到maxPendingMessages后send操作会报错，设置为true后，send操作阻塞但是不报错。建议设置为true
                .blockIfQueueFull(true)
                //设置等待接受来自broker确认消息的队列的最大大小，默认1000
                .maxPendingMessages(1000)
                //消息压缩（默认不压缩，支持四种压缩方式：LZ4，ZLIB，ZSTD，SNAPPY），consumer端不用做改动就能消费，开启后大约可以降低3/4带宽消耗和存储（官方测试）
                .compressionType(CompressionType.LZ4)
                //是否开启批量处理消息，默认true,需要注意的是enableBatching只在异步发送sendAsync生效，同步发送send失效。因此建议生产环境若想使用批处理，则需使用异步发送，或者多线程同步发送
                .enableBatching(true)
                //设置将对发送的消息进行批处理的时间段(默认1ms)；可以理解为若该时间段内批处理成功，则一个batch中的消息数量不会被该参数所影响。
                .batchingMaxPublishDelay(10, TimeUnit.MILLISECONDS)
                //批处理中允许的最大消息数。默认1000
                .batchingMaxMessages(1000)
                //向不同partition分发消息的切换频率，默认10ms，可根据batch情况灵活调整
                .roundRobinRouterBatchingPartitionSwitchFrequency(10)
                //key_Shared模式要用KEY_BASED,才能保证同一个key的message在一个batch里
                .batcherBuilder(BatcherBuilder.DEFAULT)
                //设置拦截器
                // .intercept(new MyProducerInterceptor())
                .create();
    }

    /**
     * 同步发送
     */
    public void send(String key, String data) throws PulsarClientException {
        MessageIdImpl messageId = (MessageIdImpl) producer.newMessage()
                .key(key)
                .value(data.getBytes())
                .eventTime(System.currentTimeMillis()) // (可选)业务处理时间，默认为0
                .property("p1", "v1") // (可选)自定义属性
                .property("p2", "v2") // (可选)自定义属性
                .send();
        long ledgerId = messageId.getLedgerId();
        long entryId = messageId.getEntryId();
        int partitionIndex = messageId.getPartitionIndex();
        log.info("【Producer】ledgerId={} entryId={} partition={} data={}",
                ledgerId, entryId, partitionIndex, data);
    }

    /**
     * 异步发送
     */
    public void sendAsync(String key, String data) {
        CompletableFuture<MessageId> future = producer.newMessage()
                .key(key)
                .value(data.getBytes())
                .sendAsync();
        future.handle((msgId, ex) -> {
            MessageIdImpl messageId = (MessageIdImpl) msgId;
            if (ex == null) {
                long ledgerId = messageId.getLedgerId();
                long entryId = messageId.getEntryId();
                int partitionIndex = messageId.getPartitionIndex();
                log.info("【Producer】ledgerId={} entryId={} partition={} data={}",
                        ledgerId, entryId, partitionIndex, data);
            } else {
                String msg = "【Producer】data=" + data;
                log.error(msg, ex);
            }
            return null;
        });
    }

    /**
     * 延迟发送
     */
    public void sendDelay(String key, String data, long delayMillSeconds) throws PulsarClientException {
        MessageIdImpl messageId = (MessageIdImpl) producer.newMessage()
                .key(key)
                .value(data.getBytes())
                // 方式一：延迟x毫秒发送
                .deliverAfter(delayMillSeconds, TimeUnit.MILLISECONDS)
                // 方式二：指定发送时间
                // .deliverAt(System.currentTimeMillis() + delayMillSeconds)
                .send();
        long ledgerId = messageId.getLedgerId();
        long entryId = messageId.getEntryId();
        int partitionIndex = messageId.getPartitionIndex();
        log.info("【Producer】ledgerId={} entryId={} partition={} data={}",
                ledgerId, entryId, partitionIndex, data);
    }
}

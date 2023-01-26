package com.cn.boot.sample.pulsar.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.MultiplierRedeliveryBackoff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 消费者有两种方式，
 * 方式一：自定义线程消费
 * 方式二：使用MessageListener消费
 *
 * @author Chen Nan
 */
@Slf4j
@Component
public class StringConsumer {

    @Value("${pulsar.consumer.topic}")
    private String topic;
    @Value("${pulsar.consumer.subscription}")
    private String subscription;

    @Autowired
    private PulsarClient client;
    private Consumer consumer = null;

    @PostConstruct
    public void initConsumer() throws Exception {
        try {
            //创建consumer
            consumer = client.newConsumer()
                    //指定消费主体，支持同时消费多个主题，或正则匹配主题
                    // 使用正则时，新创建的主题也能消费到，可指定新主题发现周期，默认1分钟，默认只消费持久化主题
                    .topic(topic.split(","))
                    // .topicsPattern("test.*")
                    // .patternAutoDiscoveryPeriod(1, TimeUnit.MINUTES)
                    // .subscriptionTopicsMode(RegexSubscriptionMode.AllTopics)
                    //指定订阅名称
                    .subscriptionName(subscription)
                    //消费方式二：MessageListener
                    .messageListener(new MessageListener<byte[]>() {
                        @Override
                        public void received(Consumer<byte[]> consumer, Message<byte[]> msg) {
                            handle(msg);
                        }
                    })
                    //设置批量接收消息，每100毫秒或消息达到10M接收接收一次消息
                    // .batchReceivePolicy(BatchReceivePolicy.builder()
                    //         .maxNumMessages(-1)
                    //         .maxNumBytes(10 * 1024 * 1024)
                    //         .timeout(100, TimeUnit.MILLISECONDS)
                    //         .build())
                    //指定订阅类型
                    // Exclusive 独占（默认）：相同名称的消费者组只允许起一个消费者;
                    // Failover 灾备：相同名称的消费者组可以起多个消费者实现灾备（主备），当主消费者挂掉后，备消费者可以继续消费消息
                    // Shared 共享：相同名称的消费者组可以起多个消费者，消息以轮询方式让多个消费者都能处理消息，一条消息只会由其中一个消费者处理
                    // Key_Shared 按key共享：类似Shared，不同的是Key_Shared保证相同的key发往同一个消费者
                    .subscriptionType(SubscriptionType.Shared)
                    //缓冲队列大小，默认1000，Consumer端有一个队列，用于接收从broker端push过来的消息，receive时从缓冲区出队
                    .receiverQueueSize(1000)
                    //指定从哪里开始消费，Latest/Earliest，不指定时从上次消费位置开始消费
                    // .subscriptionInitialPosition(SubscriptionInitialPosition.Latest)
                    //指定消费超时时长（如果业务不支持幂等，则不推荐，可能业务处理较慢，最终处理成功，但已经超过超时时间，导致重复消费，推荐使用negativeAck）
                    // 超时后broker重新发送消息给consumer，默认0不超时（如果设置了死信队列，则自动设置为30s），可指定间隔或倍数延迟（如1s、2s、4s、8s...）
                    // .ackTimeout(10, TimeUnit.SECONDS)
                    // .ackTimeoutRedeliveryBackoff(MultiplierRedeliveryBackoff.builder()
                    //         .minDelayMs(1000)
                    //         .maxDelayMs(60000)
                    //         .multiplier(2)
                    //         .build())
                    //指定消费失败后延迟多久broker重新发送消息给consumer，默认60s，可指定间隔或倍数延迟（如1s、2s、4s、8s...）
                    // .negativeAckRedeliveryDelay(5, TimeUnit.SECONDS)
                    .negativeAckRedeliveryBackoff(MultiplierRedeliveryBackoff.builder()
                            .minDelayMs(1000)
                            .maxDelayMs(60000)
                            .multiplier(2)
                            .build())
                    //开启消息重试，目前仅支持Shared订阅类型，开启后当前consumer会自动订阅retry主题，消息的property中会携带当前重试次数和重试间隔
                    // .enableRetry(true)
                    //死信策略，目前仅支持Shared订阅类型，重试次数不包含首次，不指定死信topic则默认为<topicname>-<subscriptionname>-DLQ
                    .deadLetterPolicy(DeadLetterPolicy.builder()
                            .maxRedeliverCount(2)
                            .deadLetterTopic("test-DLQ")
                            .retryLetterTopic("test-retry")
                            .initialSubscriptionName("my-sub")
                            .build())
                    //拦截器
                    // .intercept(new MyConsumerInterceptor())
                    .subscribe();

            //消费方式一：线程
            // new Thread(() -> {
            //     try {
            //         //消费消息
            //         while (true) {
            //             Message message = consumer.receive();
            //             handle(message);
            //         }
            //     } catch (Exception e) {
            //         log.error("【StringConsumer】消费Pulsar数据异常：", e);
            //     }
            // }).start();
        } catch (Exception e) {
            log.error("【StringConsumer】Pulsar初始化异常：", e);
            throw e;
        }
    }

    /**
     * (可选)线程池异步处理Pulsar推送的数据
     */
//    @Async("threadPoolTaskExecutor")
    public void handle(Message message) {
        String key = message.getKey();
        String msg = new String(message.getData());

        if (StringUtils.isNotEmpty(msg)) {
            try {
                String producerName = message.getProducerName();
                long eventTime = message.getEventTime();
                long publishTime = message.getPublishTime();
                String topicName = message.getTopicName();
                Map properties = message.getProperties();
                log.info("【StringConsumer】收到数据 key={} " +
                                "msg={} " +
                                "producerName={} " +
                                "eventTime={} " +
                                "publishTime={} " +
                                "topicName={} ",
                        key, msg, producerName, eventTime, publishTime, topicName);
                for (Object propertyKey : properties.keySet()) {
                    log.info("【StringConsumer】propertyKey={} value={}", propertyKey, properties.get(propertyKey));
                }
                if (StringUtils.equals("error", msg) && !StringUtils.containsIgnoreCase(topicName, "DLQ")) {
                    throw new RuntimeException("业务处理异常");
                }
                consumer.acknowledge(message);
                // 累计ack，对于batch消息，可以acknowledge单条ack，也可以只ack最后一条，如果未ack，则批次内所有消息都会重新消费
                // 订阅类型不能是Share，只能独占
                // consumer.acknowledgeCumulative(message);
            } catch (Exception e) {
                log.error("【StringConsumer】消费Pulsar数据异常，key【{}】，msg【{}】：", key, msg, e);
                consumer.negativeAcknowledge(message);
                // try {
                //     // 触发消息retry，与negativeAcknowledge相比，重试信主题更适合于需要大量重试且重试间隔可配置的消息。
                //     // 因为重试信主题中的消息被持久化到BookKeeper，而negativeAcknowledge需要重试的消息被缓存在客户端。
                //     consumer.reconsumeLater(message, 1, TimeUnit.SECONDS);
                // } catch (PulsarClientException e1) {
                //     e1.printStackTrace();
                // }
            }
        }
    }
}

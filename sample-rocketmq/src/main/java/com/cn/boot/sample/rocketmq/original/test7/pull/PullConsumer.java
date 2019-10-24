package com.cn.boot.sample.rocketmq.original.test7.pull;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.rocketmq.constant.MqConstant;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * PullConsumer，使用拉取方式订阅消息
 * 注意：拉取方式的offset需要自己维护，正常情况下要持久化到磁盘，示例中仅保存到内存，重启后又变成0，会重新消费消息
 *
 * @author Chen Nan
 */
@Slf4j
//@Component
public class PullConsumer {
    public static final String TAG = "test7";

    private static DefaultMQPullConsumer consumer;
    private static Map<MessageQueue, Long> queueOffset = new HashMap<>();

    @PostConstruct
    public void init() throws MQClientException {
        log.info("【PullConsumer】init");

        consumer = new DefaultMQPullConsumer(TAG + "_consumer_group");
        consumer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);
        consumer.start();

        Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues(TAG + "_topic");
        // 每次拉取消息最大数量
        int pullCount = 2;
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        singleThreadPool.execute(() -> {
            while (true) {
                for (MessageQueue messageQueue : messageQueues) {
                    SINGLE_MQ:
                    while (true) {
                        try {
                            PullResult pull = consumer.pull(messageQueue, "*", getMessageQueueOffset(messageQueue), pullCount);
                            putMessageQueueOffset(messageQueue, pull.getNextBeginOffset());
                            switch (pull.getPullStatus()) {
                                case FOUND:
                                    List<MessageExt> msgList = pull.getMsgFoundList();
                                    for (MessageExt msg : msgList) {
                                        log.info("【PullProducer】msg={}", JSONUtil.toJsonStr(msg));
                                        String body = new String(msg.getBody());
                                        log.info("【PullProducer】body={}", body);
                                    }
                                    break;
                                case NO_NEW_MSG:
                                    break SINGLE_MQ;
                                case NO_MATCHED_MSG:
                                    break;
                                case OFFSET_ILLEGAL:
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            log.error("【PullConsumer】error", e);
                        }
                    }
                }
            }
        });
    }

    private void putMessageQueueOffset(MessageQueue messageQueue, Long offset) {
        queueOffset.put(messageQueue, offset);
    }

    private Long getMessageQueueOffset(MessageQueue messageQueue) {
        Long offset = queueOffset.get(messageQueue);
        return offset == null ? 0 : offset;
    }

    @PreDestroy
    public void destroy() {
        log.info("【PullConsumer】destroy");
        consumer.shutdown();
    }
}

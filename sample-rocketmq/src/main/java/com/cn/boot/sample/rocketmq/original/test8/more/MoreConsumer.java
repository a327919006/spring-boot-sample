package com.cn.boot.sample.rocketmq.original.test8.more;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.rocketmq.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * selector，指定发送到的队列
 *
 * @author Chen Nan
 */
@Slf4j
//@Component
public class MoreConsumer {
    public static final String TAG = "test8";

    private static DefaultMQPushConsumer consumer;

    @PostConstruct
    public void init() throws MQClientException {
        log.info("【MoreConsumer】init");

        consumer = new DefaultMQPushConsumer(TAG + "_consumer_group");

        consumer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        // 订阅多个主题
        consumer.subscribe(TAG + "_0_topic", "*");
        consumer.subscribe(TAG + "_1_topic", "*");

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            MessageExt msg = msgs.get(0);
            log.info("【MoreConsumer】msg={}", JSONUtil.toJsonStr(msg));
            String body = new String(msg.getBody());

            log.info("【MoreConsumer】body={}, tag={}", body, msg.getTags());
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
    }

    @PreDestroy
    public void destroy() {
        log.info("【MoreConsumer】destroy");
        consumer.shutdown();
    }
}

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

    @PostConstruct
    public void init() throws MQClientException {
        log.info("【MoreConsumer】init");

        // 订阅多个topic，方式一：公用线程池
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(TAG + "_consumer_group");
        consumer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        // 订阅多个主题
        consumer.subscribe(TAG + "_" + 0 + "_topic", "*");
        consumer.subscribe(TAG + "_" + 1 + "_topic", "*");

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            MessageExt msg = msgs.get(0);
            log.info("【MoreConsumer】msg={}", JSONUtil.toJsonStr(msg));
            String body = new String(msg.getBody());

            log.info("【MoreConsumer】body={}, tag={}", body, msg.getTags());
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();

        // 订阅多个topic，方式二：每个topic独立线程池
//        for (int i = 0; i < 2; i++) {
//            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(TAG + "_consumer_group_" + i);
//
//            consumer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);
//
//            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
//
//            // 订阅多个主题
//            consumer.subscribe(TAG + "_" + i + "_topic", "*");
//
//            consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
//                MessageExt msg = msgs.get(0);
//                log.info("【MoreConsumer】msg={}", JSONUtil.toJsonStr(msg));
//                String body = new String(msg.getBody());
//
//                log.info("【MoreConsumer】body={}, tag={}", body, msg.getTags());
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            });
//
//            consumer.start();
//        }
    }
}

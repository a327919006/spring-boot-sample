package com.cn.boot.sample.rocketmq.test;

import cn.hutool.core.thread.ThreadUtil;
import com.cn.boot.sample.rocketmq.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
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
public class TestConsumer {
    public static final String TAG = "test";

    private static DefaultMQPushConsumer consumer;

    @PostConstruct
    public void init() throws MQClientException {
        log.info("【TestConsumer】init");

        consumer = new DefaultMQPushConsumer(TAG + "_consumer_group");

        consumer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        consumer.subscribe(TAG + "_topic", "*");

        consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
            MessageExt msg = msgs.get(0);
            String body = new String(msg.getBody());
            log.info("【TestConsumer】开始={}", body);
            ThreadUtil.sleep(100);
            log.info("【TestConsumer】完成={}", body);

            return ConsumeOrderlyStatus.SUCCESS;
        });

        consumer.start();
    }

    @PreDestroy
    public void destroy() {
        log.info("【TestConsumer】destroy");
        consumer.shutdown();
    }
}

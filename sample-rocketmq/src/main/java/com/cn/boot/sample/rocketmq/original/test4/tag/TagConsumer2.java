package com.cn.boot.sample.rocketmq.original.test4.tag;

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
 * 根据tag消费
 *
 * @author Chen Nan
 */
@Slf4j
//@Component
public class TagConsumer2 {
    public static final String TAG = "test4";

    private static DefaultMQPushConsumer consumer;

    @PostConstruct
    public void init() throws MQClientException {
        log.info("【TagConsumer2】init");

        consumer = new DefaultMQPushConsumer(TAG + "_consumer_group2");

        consumer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        consumer.subscribe(TAG + "_topic", "test4_0||test4_3||test4_4");

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            MessageExt msg = msgs.get(0);
            log.info("【TagConsumer2】msg={}", JSONUtil.toJsonStr(msg));
            // 处理业务
            String body = new String(msg.getBody());

            log.info("【TagConsumer2】body={}", body);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
    }

    @PreDestroy
    public void destroy() {
        log.info("【TagConsumer2】destroy");
        consumer.shutdown();
    }
}

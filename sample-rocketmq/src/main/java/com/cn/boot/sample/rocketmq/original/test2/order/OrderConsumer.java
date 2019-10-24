package com.cn.boot.sample.rocketmq.original.test2.order;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
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
 * 顺序消息（MessageListenerOrderly）
 * 不是绝对顺序，
 * 默认会启动4个消费者，不能保证4个线程处理任务的顺序，只是前4个会先执行，后4个后执行
 * 如果不需要顺序可使用test1中的MessageListenerConcurrently
 *
 * @author Chen Nan
 */
@Slf4j
//@Component
public class OrderConsumer {
    public static final String TAG = "test2";

    private static DefaultMQPushConsumer consumer;

    @PostConstruct
    public void init() throws MQClientException {
        log.info("【OrderConsumer】init");

        consumer = new DefaultMQPushConsumer(TAG + "_consumer_group");

        consumer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        consumer.subscribe(TAG + "_topic", "*");

        consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
            MessageExt msg = msgs.get(0);
            log.info("【OrderConsumer】msg={}", JSONUtil.toJsonStr(msg));
            // 处理业务
            String body = new String(msg.getBody());

            log.info("【OrderConsumer】body={}", body);

            ThreadUtil.sleep(1000);
            return ConsumeOrderlyStatus.SUCCESS;
        });

        consumer.start();
    }

    @PreDestroy
    public void destroy() {
        log.info("【OrderConsumer】destroy");
        consumer.shutdown();
    }
}

package com.cn.boot.sample.rocketmq.original.test10.broadcast;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.rocketmq.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;


/**
 * 广播消息消费者
 * 消费者需设置为广播模式consumer.setMessageModel(MessageModel.BROADCASTING);
 * 启动多个consumer，每个consumer都能收到相同的消息
 *
 * @author Chen Nan
 */
@Slf4j
public class BroadcastConsumer {
    public static final String TAG = "test10";

    private static DefaultMQPushConsumer consumer;

    public static void main(String[] args) throws MQClientException {
        log.info("【BroadcastConsumer】init");

        consumer = new DefaultMQPushConsumer(TAG + "_consumer_group");

        consumer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);

        // 设置为广播模式
        consumer.setMessageModel(MessageModel.BROADCASTING);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        consumer.subscribe(TAG + "group", "*");

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            MessageExt msg = msgs.get(0);
            log.info("【BroadcastConsumer】msg={}", JSONUtil.toJsonStr(msg));
            // 处理业务
            String body = new String(msg.getBody());

            log.info("【BroadcastConsumer】body={}", body);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
    }
}

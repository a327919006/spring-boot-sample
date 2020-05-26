package com.cn.boot.sample.rocketmq.original.test12.fromnow;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.rocketmq.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;


/**
 * 从当前时间开始消费-消息消费者
 * 举例：服务器重启后，业务上无需消费重启期间的所有MQ消息
 * 为了防止重启后大量此类MQ消息要处理，设置为从当前时间开始消费
 * <p>
 * ----------------------------------
 * 理想很丰满，现实很骨干。
 * 从源码分析（RebalancePushImpl），从某个时间开始消费，只有首次启动有效，非首次都是从offsetStore中获取上次消费位置开始消费
 * 解决方式：
 * 1、每次换个consumerGroup名称，优点：简单，高效；缺点：消费者集群情况下，不能修改名称。而且控制台会有很多废弃的消费者组，维护麻烦。
 * 2、官网使用的方式：https://github.com/apache/rocketmq/blob/master/docs/cn/best_practice.md
 * 根据msg的offset和maxOffset不走业务流程（或者根据消息时间过滤），直接消费掉大量MQ
 *
 * @author Chen Nan
 */
@Slf4j
public class FromNowConsumer {
    public static final String TAG = "test12";

    private static DefaultMQPushConsumer consumer;

    private static final long START_TIME = System.currentTimeMillis();

    public static void main(String[] args) throws MQClientException {
        log.info("【FromNowConsumer】init");

        consumer = new DefaultMQPushConsumer(TAG + "_consumer_group");

        consumer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);
        // 广播模式
        consumer.setMessageModel(MessageModel.BROADCASTING);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        consumer.subscribe(TAG + "_topic", "*");

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            MessageExt msg = msgs.get(0);

            // 按时间过滤
            if (START_TIME > msg.getBornTimestamp()) {
                log.info("startTime={} msgTime={}", START_TIME, msg.getBornTimestamp());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

            // 按offset过滤，从最近1000条开始消费
            // 当前消息offset
            long offset = msgs.get(0).getQueueOffset();
            // 最大消息offset
            long maxOffset = Long.parseLong(msgs.get(0).getProperty(MessageConst.PROPERTY_MAX_OFFSET));
            int count = 1000;
            if (maxOffset - offset > count) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

            log.info("【FromNowConsumer】msg={}", JSONUtil.toJsonStr(msg));
            // 处理业务
            String body = new String(msg.getBody());

            log.info("【FromNowConsumer】body={}", body);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
    }
}

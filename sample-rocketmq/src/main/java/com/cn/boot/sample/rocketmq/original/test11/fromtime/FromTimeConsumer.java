package com.cn.boot.sample.rocketmq.original.test11.fromtime;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.rocketmq.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;


/**
 * 从某个时间开始消费-消息消费者
 * 举例：服务器重启后，从一天前的消息开始消费
 *
 * @author Chen Nan
 */
@Slf4j
public class FromTimeConsumer {
    public static final String TAG = "test11";

    private static DefaultMQPushConsumer consumer;

    public static void main(String[] args) throws MQClientException {
        log.info("【FromTimeConsumer】init");

        consumer = new DefaultMQPushConsumer(TAG + "_consumer_group_" + System.currentTimeMillis());

        consumer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);
        // 广播模式
        consumer.setMessageModel(MessageModel.BROADCASTING);

        // 设置从某个时间开始消费，本次设置有效，重启后会从上次读取位置继续读，不会按指定时间开始读
        // 如果需要每次都从某个时间开始消费，生产者组名称每次需改变，如上代码，加上时间戳
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
        // 指定时间 如20200518051949
        String timestamp = UtilAll.timeMillisToHumanString3(System.currentTimeMillis() - (1000 * 60 * 60 * 12));
        consumer.setConsumeTimestamp(timestamp);
        log.info("【FromTimeConsumer】timestamp={}", timestamp);

        consumer.subscribe(TAG + "_topic", "*");

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            MessageExt msg = msgs.get(0);
            log.info("【FromTimeConsumer】msg={}", JSONUtil.toJsonStr(msg));
            // 处理业务
            String body = new String(msg.getBody());

            log.info("【FromTimeConsumer】body={}", body);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();
    }
}

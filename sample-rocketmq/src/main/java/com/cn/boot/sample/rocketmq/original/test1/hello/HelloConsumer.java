package com.cn.boot.sample.rocketmq.original.test1.hello;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.rocketmq.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;


/**
 * 入门示例
 *
 * @author Chen Nan
 */
@Slf4j
//@Component
public class HelloConsumer {
    public static final String TAG = "test1";

    private static DefaultMQPushConsumer consumer;

    @PostConstruct
    public void init() throws MQClientException {
        log.info("【HelloConsumer】init");

        consumer = new DefaultMQPushConsumer(TAG + "_consumer_group");

        consumer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        consumer.subscribe(TAG + "_topic", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                MessageExt msg = msgs.get(0);
                // {"queueId":3,"flag":0,"sysFlag":0,"reconsumeTimes":0,"storeTimestamp":1569065308460,"msgId":"C0A81F4114C818B4AAC26B2CF52A0002","body":[72,101,108,108,111,32,82,111,99,107,101,116,77,81,33,32,116,101,115,116,49,95,50],"bornHost":"/192.168.31.65:53876","preparedTransactionOffset":0,"commitLogOffset":5853,"bodyCRC":1138950076,"topic":"test1_topic","storeSize":191,"queueOffset":2,"bornTimestamp":1569065308458,"storeHost":"/192.168.31.65:10911","properties":{"MIN_OFFSET":"0","MAX_OFFSET":"3","KEYS":"test1_1","CONSUME_START_TIME":"1569065308466","UNIQ_KEY":"C0A81F4114C818B4AAC26B2CF52A0002","TAGS":"test1"}}
                log.info("【HelloConsumer】msg={}", JSONUtil.toJsonStr(msg));
                try {
                    // 处理业务
                    String keys = msg.getKeys();
                    String body = new String(msg.getBody());

                    log.info("【HelloConsumer】body={}", body);
                    if ("test1_3".equals(keys)) {
                        // 模拟一个异常
                        int i = 1 / 0;
                    }

                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    // 获取消息已经重发的次数
                    // 重试间隔10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
                    int reconsumeTimes = msg.getReconsumeTimes();
                    log.error("【HelloConsumer】error reconsumeTimes={}, ", reconsumeTimes, e);

                    if (2 == reconsumeTimes) {
                        // 当重试达到一定次数，任无法消费，记录日志，做补偿
                        log.error("【HelloConsumer】msg={}", JSONUtil.toJsonStr(msg));
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }

                    // 返回RECONSUME_LATER，消息稍后会重发
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });

        consumer.start();
    }

    @PreDestroy
    public void destroy() {
        log.info("【HelloConsumer】destroy");
        consumer.shutdown();
    }
}

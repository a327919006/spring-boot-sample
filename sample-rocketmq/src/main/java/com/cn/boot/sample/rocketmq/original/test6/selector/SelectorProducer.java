package com.cn.boot.sample.rocketmq.original.test6.selector;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.rocketmq.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * selector，指定发送到的队列
 *
 * @author Chen Nan
 */
@Slf4j
//@Component
public class SelectorProducer {
    public static final String TAG = "test6";

    private static DefaultMQProducer producer;

    @PostConstruct
    public void init() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        log.info("【SelectorProducer】init");

        producer = new DefaultMQProducer(TAG + "_producer_group");

        producer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);

        producer.start();

        // 发送五条消息
        for (int i = 0; i < 5; i++) {
            byte[] body = ("Hello RocketMQ! " + TAG + "_" + i).getBytes();
            Message message = new Message();
            message.setTopic(TAG + "_topic"); // 主题
            message.setTags(TAG); // 标签
            message.setKeys(TAG + "_" + i); // 消息唯一标识，用户自定义
            message.setBody(body);

            SendResult sendResult = producer.send(message, (mqs, msg, arg) -> {
                log.info("【MessageQueueSelector】size = " + mqs.size());
                Integer num = (Integer) arg;
                int queueNum = num % mqs.size();
                return mqs.get(queueNum);
            }, i);
            log.info("【SelectorProducer】sendResult = {}", JSONUtil.toJsonStr(sendResult));
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        log.info("【SelectorProducer】destroy");
        producer.shutdown();
    }
}

package com.cn.boot.sample.rocketmq.original.test7.pull;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.rocketmq.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * PullConsumer，使用拉取方式订阅消息
 *
 * @author Chen Nan
 */
@Slf4j
public class PullProducer {
    public static final String TAG = "test7";

    private static DefaultMQProducer producer;

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        log.info("【PullProducer】init");

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

            SendResult sendResult = producer.send(message);
            log.info("【PullProducer】sendResult = {}", JSONUtil.toJsonStr(sendResult));
        }
        producer.shutdown();
    }
}

package com.cn.boot.sample.rocketmq.original.test10.broadcast;

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
 * 入门示例
 *
 * @author Chen Nan
 */
@Slf4j
public class BroadcastProducer {
    public static final String TAG = "test10";

    private static DefaultMQProducer producer;

    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        log.info("【BroadcastProducer】init");

        producer = new DefaultMQProducer(TAG + "_producer_group");

        producer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);

        producer.start();

        for (int i = 0; i < 10; i++) {
            byte[] body = ("Hello RocketMQ! " + TAG + "_" + i).getBytes();
            Message message = new Message();
            message.setTopic(TAG + "_topic"); // 主题
            message.setTags(TAG); // 标签
            message.setKeys(TAG + "_" + i); // 消息唯一标识，用户自定义
            message.setBody(body);

            SendResult sendResult = producer.send(message);
            log.info("【BroadcastProducer】sendResult = {}", JSONUtil.toJsonStr(sendResult));
        }
    }
}

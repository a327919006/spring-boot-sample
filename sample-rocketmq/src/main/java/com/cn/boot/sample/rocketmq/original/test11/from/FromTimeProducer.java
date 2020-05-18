package com.cn.boot.sample.rocketmq.original.test11.from;

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
 * 从某个时间开始消费-消息生产者
 * 生产者无特殊设置，与普通生产者一致，正常发送即可
 *
 * @author Chen Nan
 */
@Slf4j
public class FromTimeProducer {
    public static final String TAG = "test11";

    private static DefaultMQProducer producer;

    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        log.info("【FromTimeProducer】init");

        producer = new DefaultMQProducer(TAG + "_producer_group");

        producer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);

        producer.start();

        for (int i = 30; i < 100; i++) {
            byte[] body = ("Hello RocketMQ! " + TAG + "_" + i).getBytes();
            Message message = new Message();
            message.setTopic(TAG + "_topic"); // 主题
            message.setTags(TAG); // 标签
            message.setKeys(TAG + "_" + i); // 消息唯一标识，用户自定义
            message.setBody(body);

            SendResult sendResult = producer.send(message);
            log.info("【FromTimeProducer】sendResult = {}", JSONUtil.toJsonStr(sendResult));
        }
    }
}

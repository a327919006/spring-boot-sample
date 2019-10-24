package com.cn.boot.sample.rocketmq.original.test2.order;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.rocketmq.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 顺序消息
 *
 * @author Chen Nan
 */
@Slf4j
//@Component
public class OrderProducer {
    public static final String TAG = "test2";

    private static DefaultMQProducer producer;

    @Value("${server.port}")
    private int port;

    @PostConstruct
    public void init() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        log.info("【OrderProducer】init");

        producer = new DefaultMQProducer(TAG + "_producer_group");

        producer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);

        // 设置发送失败时的重发次数，默认2次，如果是异步发送，不重试
        producer.setRetryTimesWhenSendFailed(0);

        producer.start();


        for (int i = 0; i < 10; i++) {
            byte[] body = ("Hello RocketMQ! " + port + "_" + i).getBytes();
            Message message = new Message();
            message.setTopic(TAG + "_topic"); // 主题
            message.setTags(TAG); // 标签
            message.setKeys(TAG + "_" + i); // 消息唯一标识，用户自定义
            message.setBody(body);

            SendResult sendResult = producer.send(message);
            log.info("【OrderProducer】sendResult = {}", JSONUtil.toJsonStr(sendResult));
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        log.info("【OrderProducer】destroy");
        producer.shutdown();
    }
}

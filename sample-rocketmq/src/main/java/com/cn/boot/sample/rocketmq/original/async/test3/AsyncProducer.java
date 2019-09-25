package com.cn.boot.sample.rocketmq.original.async.test3;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.rocketmq.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 异步发送
 *
 * @author Chen Nan
 */
@Slf4j
@Component
public class AsyncProducer {
    public static final String TAG = "test3";

    private static DefaultMQProducer producer;

    @PostConstruct
    public void init() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        log.info("【AsyncProducer】init");

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

            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("【AsyncProducer】sendResult = {}", JSONUtil.toJsonStr(sendResult));
                }

                @Override
                public void onException(Throwable throwable) {
                    log.info("【AsyncProducer】error = {}", throwable.getMessage(), throwable);
                }
            });
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        log.info("【AsyncProducer】destroy");
        producer.shutdown();
    }
}

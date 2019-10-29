package com.cn.boot.sample.rocketmq.original.test8.more;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.rocketmq.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * selector，指定发送到的队列
 *
 * @author Chen Nan
 */
@Slf4j
//@Component
public class MoreProducer {
    public static final String TAG = "test8";

    private static DefaultMQProducer producer;

    @PostConstruct
    public void init() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        log.info("【MoreProducer】init");

        producer = new DefaultMQProducer(TAG + "_producer_group");

        producer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);

        producer.start();

        for (int i = 0; i < 10; i++) {
            byte[] body = ("Hello RocketMQ! " + TAG + "_" + i).getBytes();
            Message message = new Message();
            // 发送不同topic
            message.setTopic(TAG + "_" + (i%2) + "_topic");
            message.setTags(TAG + "_" + (i%2));
            message.setBody(body);

            SendResult sendResult = producer.send(message);
            log.info("【MoreProducer】sendResult = {}", JSONUtil.toJsonStr(sendResult));
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        log.info("【MoreProducer】destroy");
        producer.shutdown();
    }
}

package com.cn.boot.sample.rocketmq.test;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.api.exceptions.BusinessException;
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
 * @author Chen Nan
 */
@Slf4j
//@Component
public class TestProducer {
    public static final String TAG = "test";

    private static DefaultMQProducer producer;

    @PostConstruct
    public void init() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        log.info("【TestProducer】init");

        producer = new DefaultMQProducer(TAG + "_producer_group");

        producer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);

        producer.start();
    }

    public void send(String data) {
        try {
            byte[] body = data.getBytes();
            Message message = new Message();
            message.setTopic(TAG + "_topic"); // 主题
            message.setTags(TAG); // 标签
            message.setBody(body);

            SendResult sendResult = producer.send(message, (mqs, msg, arg) -> {
                log.info("【MessageQueueSelector】size = " + mqs.size());
                Integer num = (Integer) arg;
                int queueNum = num % mqs.size();
                return mqs.get(queueNum);
            }, 0);
            log.info("【TestProducer】sendResult = {}", JSONUtil.toJsonStr(sendResult));
        } catch (Exception e) {
            throw new BusinessException("发送MQ消息异常", e);
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        log.info("【TestProducer】destroy");
        stop();
    }

    public void stop() {
        log.info("【TestProducer】stop");
        producer.shutdown();
    }
}

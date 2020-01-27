package com.cn.boot.sample.rocketmq.original.test9.transaction;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.rocketmq.constant.MqConstant;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;

/**
 * 事务消息生产者
 *
 * @author Chen Nan
 */
@Slf4j
//@Component
public class TransactionProducer {
    public static final String TAG = "test9";

    private static TransactionMQProducer producer;

    @PostConstruct
    public void init() throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        log.info("【TransactionProducer】init");

        producer = new TransactionMQProducer(TAG + "_producer_group");
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("test9-pool-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(2, 5,
                100L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        producer.setNamesrvAddr(MqConstant.NAME_SERVER_ADDRESS);
        producer.setExecutorService(executorService);
        producer.setTransactionListener(new TransactionListenerImpl());
        producer.start();

        // 发送五条消息
        for (int i = 0; i < 5; i++) {
            byte[] body = ("Hello RocketMQ! " + TAG + "_" + i).getBytes();
            Message message = new Message();
            message.setTopic(TAG + "_topic"); // 主题
            message.setTags(TAG); // 标签
            message.setKeys(TAG + "_" + i); // 消息唯一标识，用户自定义
            message.setBody(body);

            SendResult sendResult = producer.sendMessageInTransaction(message, "自定义回调参数" + i);
            // {"traceOn":true,"regionId":"DefaultRegion","msgId":"C0A81F411BEC18B4AAC26B2653F70000","messageQueue":{"queueId":3,"topic":"test1_topic","brokerName":"Hasee-PC"},"sendStatus":"SEND_OK","queueOffset":0,"offsetMsgId":"C0A81F4100002A9F0000000000000C4A"}
            log.info("【TransactionProducer】sendResult = {}", JSONUtil.toJsonStr(sendResult));
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        log.info("【TransactionProducer】destroy");
        producer.shutdown();
    }
}

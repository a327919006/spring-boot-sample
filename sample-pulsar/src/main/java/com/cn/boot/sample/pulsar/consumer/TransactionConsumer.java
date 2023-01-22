package com.cn.boot.sample.pulsar.consumer;

import com.cn.boot.sample.pulsar.producer.TransactionProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Slf4j
// @Component
public class TransactionConsumer {

    @Autowired
    private PulsarClient client;
    @Autowired
    private TransactionProducer producer;
    private Consumer consumer = null;

    @PostConstruct
    public void initConsumer() throws Exception {
        try {
            //创建consumer
            consumer = client.newConsumer()
                    .topic("test1")
                    .subscriptionName("test_transaction")
                    .subscribe();

            // 开始消费
            new Thread(() -> {
                try {
                    start();
                } catch (Exception e) {
                    log.error("【TransactionConsumer】消费Pulsar数据异常，停止Pulsar连接：", e);
                    close();
                }
            }).start();

        } catch (Exception e) {
            log.error("Pulsar初始化异常：", e);
            throw e;
        }
    }

    private void start() throws Exception {
        //消费消息
        while (true) {
            Message message = consumer.receive();
            String key = message.getKey();
            String msg = new String(message.getData());

            if (StringUtils.isNotEmpty(msg)) {
                Transaction txn = null;
                try {
                    txn = client.newTransaction()
                            .withTransactionTimeout(5, TimeUnit.MINUTES)
                            .build()
                            .get();
                    handle(key, msg, txn);
                    consumer.acknowledge(message);
                    txn.commit();
                } catch (Exception e) {
                    log.error("【TransactionConsumer】消费Pulsar数据异常，key【{}】，msg【{}】：", key, msg, e);
                    if (txn != null) {
                        txn.abort();
                    }
                }
            }
        }
    }

    private void handle(String key, String msg, Transaction txn) throws Exception {
        log.info("【TransactionConsumer】收到数据 key={} msg={}", key, msg);
        producer.send(key + "-txn", msg + "-txn", txn);
        if (StringUtils.equals("error", msg)) {
            throw new RuntimeException("业务处理异常");
        }
    }

    private void close() {
        try {
            consumer.close();
        } catch (PulsarClientException e) {
            log.error("【TransactionConsumer】关闭Pulsar消费者失败：", e);
        }
    }
}

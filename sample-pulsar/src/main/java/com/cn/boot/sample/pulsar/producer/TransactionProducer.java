package com.cn.boot.sample.pulsar.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.transaction.Transaction;
import org.apache.pulsar.client.impl.MessageIdImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author Chen Nan
 */
@Slf4j
// @Component
public class TransactionProducer {

    @Autowired
    private PulsarClient client;
    private Producer<byte[]> producer = null;

    @PostConstruct
    public void initPulsar() throws Exception {
        //创建producer
        producer = client.newProducer()
                .topic("test2")
                .create();
    }

    /**
     * 事务发送
     */
    public void send(String key, String data, Transaction txn) throws PulsarClientException {
        MessageIdImpl messageId = (MessageIdImpl) producer.newMessage(txn)
                .key(key)
                .value(data.getBytes())
                .send();
        long ledgerId = messageId.getLedgerId();
        long entryId = messageId.getEntryId();
        int partitionIndex = messageId.getPartitionIndex();
        log.info("【TxnProducer】ledgerId={} entryId={} partition={} data={}",
                ledgerId, entryId, partitionIndex, data);
    }
}

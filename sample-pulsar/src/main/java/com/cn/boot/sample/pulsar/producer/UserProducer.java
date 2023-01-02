package com.cn.boot.sample.pulsar.producer;

import com.cn.boot.sample.api.model.po.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.impl.BatchMessageIdImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class UserProducer {
    @Value("${pulsar.url}")
    private String url;
    @Autowired
    private PulsarClient client;

    private Producer<User> producer = null;

    @PostConstruct
    public void initPulsar() throws Exception {
        producer = client.newProducer(Schema.JSON(User.class))
                .topic("test_user")
                .create();
    }

    public void send(User data) throws PulsarClientException {
        BatchMessageIdImpl messageId = (BatchMessageIdImpl) producer.newMessage()
                .value(data)
                .send();
        long ledgerId = messageId.getLedgerId();
        long entryId = messageId.getEntryId();
        int partitionIndex = messageId.getPartitionIndex();
        log.info("【Producer】ledgerId={} entryId={} partition={} data={}",
                ledgerId, entryId, partitionIndex, data);
    }
}

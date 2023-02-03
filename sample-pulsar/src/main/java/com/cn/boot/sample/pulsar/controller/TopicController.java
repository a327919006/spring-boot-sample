package com.cn.boot.sample.pulsar.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.impl.MessageIdImpl;
import org.apache.pulsar.common.policies.data.PersistentTopicInternalStats;
import org.apache.pulsar.common.policies.data.TopicStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/topic")
@Api(tags = "6、主题", produces = MediaType.APPLICATION_JSON_VALUE)
public class TopicController {

    @Autowired
    private PulsarAdmin client;

    @ApiOperation("创建无分区主题")
    @PostMapping("/")
    public RspBase<String> create(String persistent, String tenant, String namespace,
                                  String topic) throws PulsarAdminException {
        String topicStr = persistent + "://" + tenant + "/" + namespace + "/" + topic;
        client.topics().createNonPartitionedTopic(topicStr);
        return RspBase.success();
    }

    @ApiOperation("创建分区主题")
    @PostMapping("/partition")
    public RspBase<String> createPartitionTopic(String persistent, String tenant, String namespace,
                                                String topic, int partitionCount) throws PulsarAdminException {
        String topicStr = persistent + "://" + tenant + "/" + namespace + "/" + topic;
        client.topics().createPartitionedTopic(topicStr, partitionCount);
        return RspBase.success();
    }

    @ApiOperation("列表")
    @GetMapping("/")
    public RspBase<List<String>> list(String tenant, String namespace) throws PulsarAdminException {
        return RspBase.data(client.topics().getList(tenant + "/" + namespace));
    }

    @ApiOperation("修改分区数")
    @PutMapping("/partition/count")
    public RspBase<String> updatePartitionCount(String persistent, String tenant, String namespace,
                                                String topic, int partitionCount) throws PulsarAdminException {
        String topicStr = persistent + "://" + tenant + "/" + namespace + "/" + topic;
        client.topics().updatePartitionedTopic(topicStr, partitionCount);
        return RspBase.success();
    }

    @ApiOperation("删除无分区主题")
    @DeleteMapping("/")
    public RspBase<String> delete(String persistent, String tenant, String namespace,
                                  String topic) throws PulsarAdminException {
        String topicStr = persistent + "://" + tenant + "/" + namespace + "/" + topic;
        client.topics().delete(topicStr);
        return RspBase.success();
    }


    @ApiOperation("删除分区主题")
    @DeleteMapping("/partition")
    public RspBase<String> deletePartitionTopic(String persistent, String tenant, String namespace,
                                                String topic) throws PulsarAdminException {
        String topicStr = persistent + "://" + tenant + "/" + namespace + "/" + topic;
        client.topics().deletePartitionedTopic(topicStr);
        return RspBase.success();
    }

    @ApiOperation("获取统计信息")
    @GetMapping("/stats")
    public RspBase<TopicStats> getStats(String topic) throws PulsarAdminException {
        return RspBase.data(client.topics().getStats(topic));
    }

    @ApiOperation("获取统计信息")
    @GetMapping("/internalStats")
    public RspBase<PersistentTopicInternalStats> getInternalStats(String topic) throws PulsarAdminException {
        return RspBase.data(client.topics().getInternalStats(topic));
    }

    @ApiOperation("查看消息")
    @GetMapping("/peekMessages")
    public RspBase<List<String>> peekMessages(String topic, String subName, int num) throws PulsarAdminException {
        List<Message<byte[]>> messages = client.topics().peekMessages(topic, subName, num);
        List<String> list = new ArrayList<>();
        messages.forEach(msg -> list.add(new String(msg.getValue())));
        return RspBase.data(list);
    }

    @ApiOperation("跳过消息")
    @PostMapping("/skipMessages")
    public RspBase<String> skipMessages(String topic, String subName, int num) throws PulsarAdminException {
        client.topics().skipMessages(topic, subName, num);
        return RspBase.success();
    }

    @ApiOperation("跳过所有消息")
    @PostMapping("/skipAllMessages")
    public RspBase<String> skipAllMessages(String topic, String subName) throws PulsarAdminException {
        client.topics().skipAllMessages(topic, subName);
        return RspBase.success();
    }

    @ApiOperation(value = "重置游标至指定时间", notes = "重置后消费者会立刻收到该时间后的消息")
    @PostMapping("/resetCursor")
    public RspBase<String> resetCursor(String topic, String subName, long timestamp) throws PulsarAdminException {
        client.topics().resetCursor(topic, subName, timestamp);
        return RspBase.success();
    }

    @ApiOperation("获取主题下所有订阅组名称")
    @GetMapping("/getSubscriptions")
    public RspBase<List<String>> getSubscriptions(String topic) throws PulsarAdminException {
        return RspBase.data(client.topics().getSubscriptions(topic));
    }

    @ApiOperation("获取最后一条消息ID")
    @GetMapping("/getLastMessageId")
    public RspBase<MessageId> getLastMessageId(String topic) throws PulsarAdminException {
        MessageIdImpl lastMessageId = (MessageIdImpl) client.topics().getLastMessageId(topic);
        return RspBase.data(lastMessageId);
    }
}

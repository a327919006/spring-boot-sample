package com.cn.boot.sample.pulsar.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/topic")
@Api(tags = "3、主题", produces = MediaType.APPLICATION_JSON_VALUE)
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
}

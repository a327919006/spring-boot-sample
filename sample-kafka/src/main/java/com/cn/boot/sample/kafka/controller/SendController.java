package com.cn.boot.sample.kafka.controller;

import com.cn.boot.sample.kafka.constant.KafkaConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/send")
@Api(tags = "发送端管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class SendController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @ApiOperation("发送消息")
    @PostMapping
    public String sendMsg(String msg) {
        kafkaTemplate.send(KafkaConstant.TOPIC_TEST, msg);
        return msg;
    }

    @ApiOperation("发送消息")
    @PostMapping("/{topic}")
    public String sendTopicMsg(@PathVariable String topic, String msg) {
        kafkaTemplate.send(topic, msg);
        return msg;
    }
}

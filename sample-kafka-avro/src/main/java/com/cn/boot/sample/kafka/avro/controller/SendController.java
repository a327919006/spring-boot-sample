package com.cn.boot.sample.kafka.avro.controller;

import cn.hutool.core.util.IdUtil;
import com.cn.boot.sample.kafka.avro.model.UserAvro;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
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
    private KafkaTemplate<String, UserAvro> kafkaTemplate;

    @ApiOperation("发送消息")
    @PostMapping
    public UserAvro sendMsg() {
        UserAvro user = new UserAvro();
        user.setId(IdUtil.simpleUUID());
        user.setUsername("test1");
        kafkaTemplate.send("testTopic", user);
        return user;
    }
}

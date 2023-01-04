package com.cn.boot.sample.pulsar.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import com.cn.boot.sample.api.model.po.User;
import com.cn.boot.sample.pulsar.producer.StringProducer;
import com.cn.boot.sample.pulsar.producer.UserProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/send")
@Api(tags = "4、发送端管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class SendController {

    @Autowired
    private StringProducer stringProducer;
    @Autowired
    private UserProducer userProducer;

    @ApiOperation("同步发送")
    @PostMapping("/sync")
    public RspBase<String> send(String key, String msg) throws PulsarClientException {
        stringProducer.send(key, msg);
        return RspBase.success();
    }

    @ApiOperation("异步发送")
    @PostMapping("/async")
    public RspBase<String> sendAsync(String key, String msg) {
        stringProducer.sendAsync(key, msg);
        return RspBase.success();
    }


    @ApiOperation("发送对象")
    @PostMapping("/user/sync")
    public RspBase<String> sendUser(User user) throws PulsarClientException {
        userProducer.send(user);
        return RspBase.success();
    }
}

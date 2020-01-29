package com.cn.boot.sample.rocketmq.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.rocketmq.test.TestProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试发送MQ消息
 *
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/mq")
@Api(tags = "MQ发送端", produces = MediaType.APPLICATION_JSON_VALUE)
public class SendController {

    @Autowired
    private TestProducer producer;

    @ApiOperation("发送数据")
    @PostMapping("/send")
    public String send(@RequestParam String data) {
        producer.send(data);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("发送多条数据")
    @PostMapping("/send/more")
    public String sendMore(@RequestParam String data, @RequestParam int count) {
        for (int i = 0; i < count; i++) {
            producer.send(data + i);
        }
        return Constants.MSG_SUCCESS;
    }


    @ApiOperation("发送多条数据")
    @PostMapping("/stop")
    public String stop() {
        producer.stop();
        return Constants.MSG_SUCCESS;
    }
}

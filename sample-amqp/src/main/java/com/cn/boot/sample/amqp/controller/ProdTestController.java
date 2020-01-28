package com.cn.boot.sample.amqp.controller;

import com.cn.boot.sample.amqp.prod.ProdTestProducer;
import com.cn.boot.sample.api.model.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 测试发送MQ消息
 *
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/mq")
@Api(tags = "MQ发送端", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProdTestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ProdTestProducer producer;

    @ApiOperation("发送数据")
    @PostMapping("/send")
    public String send(@RequestParam String data) {
        producer.sendData(data);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("发送多条数据")
    @PostMapping("/send/more")
    public String sendMore(@RequestParam String data, @RequestParam int count) {
        for (int i = 0; i < count; i++) {
            producer.sendData(data + i);
        }
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("发送数据")
    @PostMapping("/{exchange}/{key}/send/more")
    public String sendExchange(@PathVariable String exchange, @PathVariable String key,
                               @RequestParam String data, @RequestParam int count) {
        for (int i = 0; i < count; i++) {
            rabbitTemplate.convertAndSend(exchange, key, data + i);
        }
        return Constants.MSG_SUCCESS;
    }

}

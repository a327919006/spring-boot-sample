package com.cn.boot.sample.mqtt.controller;

import cn.hutool.core.lang.ObjectId;
import com.cn.boot.sample.api.model.dto.RspBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    @Autowired
    private MqttClient mqttClient;

    @ApiOperation("发送消息")
    @PostMapping("")
    public RspBase<Integer> publish(String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttClient.publish(topic, mqttMessage);
        return RspBase.success();
    }

    @ApiOperation("订阅消息")
    @GetMapping("")
    public RspBase<Integer> insert(String topic) throws MqttException {
        mqttClient.subscribe(topic, 0, new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                log.info("topic={}, message={}", topic, new String(message.getPayload()));
            }
        });
        return RspBase.success();
    }
}

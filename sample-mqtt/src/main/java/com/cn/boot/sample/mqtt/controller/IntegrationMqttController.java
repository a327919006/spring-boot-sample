package com.cn.boot.sample.mqtt.controller;

import cn.hutool.core.lang.ObjectId;
import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.api.model.dto.RspBase;
import com.cn.boot.sample.mqtt.handler.MqttGateway;
import com.cn.boot.sample.mqtt.model.MqttMsg;
import com.cn.boot.sample.mqtt.util.MqttUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * IntegrationMqtt测试
 *
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/mqtt")
@Api(tags = "mqtt", produces = MediaType.APPLICATION_JSON_VALUE)
public class IntegrationMqttController {

    @Autowired
    private MqttGateway mqttGateway;

    @Autowired
    private MqttPahoMessageDrivenChannelAdapter adapter;

    @ApiOperation("发送消息")
    @PostMapping("")
    public RspBase<Object> publish(String topic, String message) throws Exception {
        mqttGateway.publish(topic, message);
        return RspBase.success();
    }

    @ApiOperation("发送消息并等待响应")
    @PostMapping("publishAndSubscribe")
    public RspBase<Object> publishAndSubscribe(String topic, String message) throws Exception {
        String id = ObjectId.next();
        MqttMsg mqttMsg = new MqttMsg();
        mqttMsg.setId(id);
        mqttMsg.setPayload(message);
        String data = JSONUtil.toJsonStr(mqttMsg);

        String[] topics = adapter.getTopic();
        boolean exist = ArrayUtils.contains(topics, topic + "_reply");
        if (!exist) {
            adapter.addTopic(topic + "_reply");
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        mqttGateway.publish(topic, data);
        MqttUtils.lock(id, countDownLatch);
        boolean success = countDownLatch.await(2, TimeUnit.SECONDS);
        if (success) {
            RspBase<Object> result = MqttUtils.getResult(id);
            if (result != null) {
                return result;
            } else {
                return RspBase.success();
            }
        } else {
            return RspBase.fail("超时");
        }
    }

    @ApiOperation("订阅主题")
    @GetMapping("")
    public RspBase<Integer> subscribe(String topic) {
        String[] topics = adapter.getTopic();
        boolean exist = ArrayUtils.contains(topics, topic);
        if (!exist) {
            adapter.addTopic(topic);
        }
        return RspBase.success();
    }
}

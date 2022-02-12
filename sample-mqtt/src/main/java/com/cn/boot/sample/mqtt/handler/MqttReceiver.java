package com.cn.boot.sample.mqtt.handler;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.api.model.dto.RspBase;
import com.cn.boot.sample.mqtt.model.MqttMsg;
import com.cn.boot.sample.mqtt.util.MqttUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

/**
 * Mqtt消息处理器
 *
 * @author Chen Nan
 */
@Slf4j
@Component
public class MqttReceiver {

    @Autowired
    private MqttGateway mqttGateway;

    //使用ServiceActivator 指定接收消息的管道为 mqttInboundChannel，投递到mqttInboundChannel管道中的消息会被该方法接收并执行
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler handleMessage() {
        return message -> {
            String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
            String payload = message.getPayload().toString();
            log.info("topic={}, message={}", topic, payload);
            MqttMsg mqttMsg = JSONUtil.toBean(payload, MqttMsg.class);
            if (StringUtils.endsWith(topic, "post")) {
                // 收到mqtt消息后，发回一个reply消息
                mqttMsg.setPayload("ok");
                String data = JSONUtil.toJsonStr(mqttMsg);
                String replyTopic = topic + "_reply";
                mqttGateway.publish(replyTopic, data);
            } else {
                // 收到reply消息后，释放同步等待锁
                RspBase<Object> result = new RspBase<>();
                result.setMsg(mqttMsg.getPayload());
                MqttUtils.unlock(mqttMsg.getId(), result);
            }
        };
    }
}

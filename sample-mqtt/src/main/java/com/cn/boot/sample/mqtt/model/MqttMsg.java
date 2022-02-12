package com.cn.boot.sample.mqtt.model;

import lombok.Data;

/**
 * @author Chen Nan
 */
@Data
public class MqttMsg {
    private String id;
    private String payload;
}

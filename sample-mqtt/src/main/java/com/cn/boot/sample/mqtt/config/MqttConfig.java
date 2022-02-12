package com.cn.boot.sample.mqtt.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MqttClient配置类
 *
 * @author Chen Nan
 */
@Slf4j
@Configuration
public class MqttConfig {

    @Autowired
    private MqttProperties properties;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{properties.getHost()});
        options.setUserName(properties.getUsername());
        options.setPassword(properties.getPassword().toCharArray());
        options.setConnectionTimeout(properties.getConnectionTimeout());///默认：30
        options.setAutomaticReconnect(properties.getAutomaticReconnect());//默认：false
        options.setCleanSession(properties.getCleanSession());//默认：true
        return options;
    }

    @Bean
    public MqttClient mqttClient() throws MqttException {
        log.info("init mqtt client start");
        MqttClient mqttClient = new MqttClient(properties.getHost(), properties.getClientId(), new MemoryPersistence());
        mqttClient.connect(mqttConnectOptions());
        log.info("init mqtt client success");
        return mqttClient;
    }
}

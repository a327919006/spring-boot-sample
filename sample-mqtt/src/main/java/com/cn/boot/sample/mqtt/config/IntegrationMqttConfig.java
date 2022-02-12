package com.cn.boot.sample.mqtt.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @author Chen Nan
 */
@Slf4j
@Configuration
@IntegrationComponentScan
public class IntegrationMqttConfig {

    @Autowired
    private MqttProperties properties;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{properties.getHost()});
        options.setUserName(properties.getUsername());
        options.setPassword(properties.getPassword().toCharArray());
        options.setConnectionTimeout(properties.getConnectionTimeout());
        options.setAutomaticReconnect(properties.getAutomaticReconnect());
        options.setCleanSession(properties.getCleanSession());

        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel errorChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        String clientId = properties.getClientId() + "_outbound";
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, mqttClientFactory());
        messageHandler.setAsync(true);
        return messageHandler;
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter adapter(MqttPahoClientFactory factory) {
        String clientId = properties.getClientId() + "_inbound";
        return new MqttPahoMessageDrivenChannelAdapter(clientId, factory);
    }

    @Bean
    public MessageProducer mqttInbound(MqttPahoMessageDrivenChannelAdapter adapter) {
        //入站投递的通道
        adapter.setOutputChannel(mqttInboundChannel());
        adapter.setErrorChannel(errorChannel());
        adapter.setCompletionTimeout(properties.getConnectionTimeout());
        adapter.setQos(1);
        return adapter;
    }
}

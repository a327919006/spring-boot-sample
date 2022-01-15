package com.cn.boot.sample.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttProperties {

    /**
     * 地址
     */
    private String host;

    /**
     * clientId
     */
    private String clientId;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 设置连接超时值 默认30秒
     */
    private Integer connectionTimeout;

    /**
     * 设置客户端是否将自动尝试重新连接到服务器，如果连接丢失。
     */
    private Boolean automaticReconnect;

    /**
     * 设置客户端和服务器是否应该记住重启和重新连接时的状态。
     */
    private Boolean cleanSession;

}

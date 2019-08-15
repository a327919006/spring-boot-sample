package com.cn.boot.sample.wechat.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {
    private String token;
    private String appId;
    private String appSecret;

}

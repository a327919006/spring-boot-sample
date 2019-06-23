package com.cn.boot.sample.security.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Data
@Component
@ConfigurationProperties(prefix = "boot.sample.security")
public class SecurityProperties {
    private BrowserProperties browser = new BrowserProperties();

}

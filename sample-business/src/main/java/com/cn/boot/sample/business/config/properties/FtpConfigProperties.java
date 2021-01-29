package com.cn.boot.sample.business.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Chen Nan
 */
@Data
@Component
public class FtpConfigProperties {
    private String ip;
    private int port;
    private String username;
    private String password;

}

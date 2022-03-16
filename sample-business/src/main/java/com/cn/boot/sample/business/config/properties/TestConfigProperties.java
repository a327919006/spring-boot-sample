package com.cn.boot.sample.business.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Data
@Component
@ConfigurationProperties(prefix = "test.config")
public class TestConfigProperties {
    private String address;
    private List<UserProperties> users;
    private FtpConfigProperties ftp;
    private Map<String, String> data;

}

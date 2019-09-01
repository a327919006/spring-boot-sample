package com.cn.boot.sample.apollo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Chen Nan
 */
@Data
@Component
@ConfigurationProperties("test2.config")
public class Test2ConfigProperties {
    private String name;

    private List<UserProperties> users;
}

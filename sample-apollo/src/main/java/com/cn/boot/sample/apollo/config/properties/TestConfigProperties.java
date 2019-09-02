package com.cn.boot.sample.apollo.config.properties;

import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Chen Nan
 */
@Data
@Component
public class TestConfigProperties {
    @Value("${test.config.name}")
    private String name;

    @ApolloJsonValue("${test.config.users:[]}")
    private List<UserProperties> users;
}

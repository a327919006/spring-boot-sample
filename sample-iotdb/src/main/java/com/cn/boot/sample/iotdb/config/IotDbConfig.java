package com.cn.boot.sample.iotdb.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.iotdb.session.pool.SessionPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Slf4j
@Configuration
public class IotDbConfig {
    @Value("${spring.iotdb.username:root}")
    private String username;

    @Value("${spring.iotdb.password:root}")
    private String password;

    @Value("${spring.iotdb.ip:127.0.0.1}")
    private String ip;

    @Value("${spring.iotdb.port:6667}")
    private int port;

    @Value("${spring.iotdb.maxSize:10}")
    private int maxSize;

    @Bean
    public SessionPool sessionPool() {
        return new SessionPool(ip, port, username, password, maxSize);
    }
}

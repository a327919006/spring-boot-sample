package com.cn.boot.sample.tdengine.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Chen Nan
 */
@Slf4j
@Configuration
public class TDEngineConfig {
    @Value("${spring.tdengine.username}")
    private String username;

    @Value("${spring.tdengine.password}")
    private String password;

    @Value("${spring.tdengine.ip}")
    private String ip;

    @Value("${spring.tdengine.port}")
    private int port;

    @Bean
    public Connection connection() throws Exception {
        Class.forName("com.taosdata.jdbc.TSDBDriver");
        String jdbcUrl = "jdbc:TAOS://" + ip + ":" + port +
                "/demo?user=" + username + "&password=" + password +
                "&charset=UTF-8&locale=en_US.UTF-8&timezone=UTC-8";
        return DriverManager.getConnection(jdbcUrl);
    }
}

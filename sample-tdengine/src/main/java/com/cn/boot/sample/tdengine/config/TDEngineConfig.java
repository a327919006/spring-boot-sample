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
    @Value("${spring.tdengine.url}")
    private String url;
    @Value("${spring.tdengine.driver}")
    private String driver;

    @Bean
    public Connection connection() throws Exception {
        Class.forName(driver);
        return DriverManager.getConnection(url);
    }
}

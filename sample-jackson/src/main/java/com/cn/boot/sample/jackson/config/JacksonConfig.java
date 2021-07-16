package com.cn.boot.sample.jackson.config;


import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Configuration
public class JacksonConfig {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        return new HttpMessageConverters(new JacksonHttpMessageConverter());
    }
}
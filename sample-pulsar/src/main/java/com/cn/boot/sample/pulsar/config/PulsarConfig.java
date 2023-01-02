package com.cn.boot.sample.pulsar.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Slf4j
@Configuration
public class PulsarConfig {

    @Value("${pulsar.url}")
    private String url;
    @Value("${pulsar.admin-url}")
    private String adminUrl;

    @Bean
    public PulsarClient pulsarClient() throws Exception {
        return PulsarClient.builder()
                .serviceUrl(url)
                .build();
    }

    @Bean
    public PulsarAdmin pulsarAdmin() throws Exception {
        return PulsarAdmin.builder()
                .serviceHttpUrl(adminUrl)
                .build();
    }

}

package com.cn.boot.sample.pulsar.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminBuilder;
import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.ClientBuilder;
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
    @Value("${pulsar.token}")
    private String token;
    @Value("${pulsar.transaction}")
    private boolean transaction;

    @Bean
    public PulsarClient pulsarClient() throws Exception {
        ClientBuilder clientBuilder = PulsarClient.builder()
                .serviceUrl(url);
        // 未开启Authentication，不需要设置token
        if (StringUtils.isNotEmpty(token)) {
            clientBuilder.authentication(AuthenticationFactory.token(token));
        }
        // 开启事务
        clientBuilder.enableTransaction(transaction);
        return clientBuilder.build();
    }

    @Bean
    public PulsarAdmin pulsarAdmin() throws Exception {
        PulsarAdminBuilder pulsarAdminBuilder = PulsarAdmin.builder()
                .serviceHttpUrl(adminUrl);
        // 未开启Authentication，不需要设置token
        if (StringUtils.isNotEmpty(token)) {
            pulsarAdminBuilder.authentication(AuthenticationFactory.token(token));
        }
        return pulsarAdminBuilder.build();
    }

}

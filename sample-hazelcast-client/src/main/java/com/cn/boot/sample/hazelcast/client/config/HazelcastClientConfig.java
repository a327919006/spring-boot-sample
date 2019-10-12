package com.cn.boot.sample.hazelcast.client.config;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Configuration
public class HazelcastClientConfig {

    @Bean
    public ClientConfig hazelCastConfig() {
        ClientConfig config = new ClientConfig();

        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        clientNetworkConfig.addAddress("127.0.0.1:5701");

        config.setNetworkConfig(clientNetworkConfig);
        return config;
    }
}

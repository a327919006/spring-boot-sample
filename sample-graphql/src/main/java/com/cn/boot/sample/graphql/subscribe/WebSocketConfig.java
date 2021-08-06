package com.cn.boot.sample.graphql.subscribe;

import com.cn.boot.sample.graphql.config.GraphqlConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author Chen Nan
 */
@Configuration
@AutoConfigureAfter(value = GraphqlConfig.class)
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

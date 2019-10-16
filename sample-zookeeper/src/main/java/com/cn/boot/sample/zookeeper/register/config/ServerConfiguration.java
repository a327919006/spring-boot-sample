package com.cn.boot.sample.zookeeper.register.config;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.zookeeper.register.properties.ServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Chen Nan
 */
@SuppressWarnings("unchecked")
@Configuration
public class ServerConfiguration {
    @Value("${server.config}")
    private String serverConfig;

    @Bean
    public ServerConfig init() throws UnknownHostException {
        ServerConfig serverConfig = JSONUtil.toBean(this.serverConfig, ServerConfig.class);

        InetAddress addr = InetAddress.getLocalHost();
        String hostName = addr.getHostName(); //获取本机计算机名称

        serverConfig.setHostName(hostName);
        return serverConfig;
    }
}

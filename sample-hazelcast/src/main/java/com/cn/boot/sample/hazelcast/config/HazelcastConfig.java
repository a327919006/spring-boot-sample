package com.cn.boot.sample.hazelcast.config;

import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Configuration
public class HazelcastConfig {

    @Bean
    public Config hazelCastConfig() {
        // 配置监控中心
        ManagementCenterConfig centerConfig = new ManagementCenterConfig()
                .setEnabled(true)
                .setUrl("http://localhost:8080/hazelcast-mancenter");

        // map配置
        MapConfig mapConfig = new MapConfig()
                .setName("default")
                .setBackupCount(2)
                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setTimeToLiveSeconds(-1);

        // 网络配置
        NetworkConfig networkConfig = new NetworkConfig().setJoin(
                new JoinConfig()
                        .setMulticastConfig(new MulticastConfig().setEnabled(false))
                        .setTcpIpConfig(
                                new TcpIpConfig()
                                        .setEnabled(true)
                                        .addMember("192.168.6.44:5701")
                                        .addMember("192.168.6.44.5702")
                                        .addMember("192.168.6.44.5703")
                                        .addMember("192.168.6.44.5704")
                        ));

        Config config = new Config();
        config.setInstanceName("hazelcast-instance")
                .addMapConfig(mapConfig)
                .setManagementCenterConfig(centerConfig)
                .setNetworkConfig(networkConfig);
        return config;
    }
}

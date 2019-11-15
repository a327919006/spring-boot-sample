package com.cn.boot.sample.hazelcast.client.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.*;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Configuration
public class HazelcastClientConfig {

    @Bean
    public HazelcastInstance hazelcastInstance() {
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
        clientNetworkConfig.addAddress("127.0.0.1:5701", "127.0.0.1:5702");

        ClientConfig config = new ClientConfig();
        config.setNetworkConfig(clientNetworkConfig);

        System.setProperty("hazelcast.client.statistics.enabled", "true");
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(config);

        // 数据处理器，缓存数据增删改查到数据库
        MapStoreConfig testMapStoreConfig = new MapStoreConfig()
                .setEnabled(true)
                .setClassName("com.cn.boot.sample.hazelcast.loader.TestDataLoader2");
        // map配置
        MapConfig mapConfig = new MapConfig()
                .setName("test2") // map名称
                .setBackupCount(2) // 数据备份数量，默认1
                .setMaxSizeConfig(new MaxSizeConfig(1000000, MaxSizeConfig.MaxSizePolicy.PER_NODE)) // 最大数据量，默认int最大值
                .setEvictionPolicy(EvictionPolicy.LRU) // 淘汰策略
                .setTimeToLiveSeconds(60) // 数据过期时长
                .setMapStoreConfig(testMapStoreConfig)
                ;

        hazelcastInstance.getConfig().addMapConfig(mapConfig);
        return hazelcastInstance;
    }
}

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
        // 可选，配置监控中心
        ManagementCenterConfig centerConfig = new ManagementCenterConfig()
                .setEnabled(true)
                .setUrl("http://localhost:8080/hazelcast-mancenter");

        // 可选，数据处理器，缓存数据增删改查到数据库
        MapStoreConfig testMapStoreConfig = new MapStoreConfig()
                .setEnabled(true)
                .setClassName("com.cn.boot.sample.hazelcast.loader.TestDataLoader");
        // 可选，map配置
        MapConfig mapConfig = new MapConfig()
                .setName("test") // map名称
                .setBackupCount(2) // 数据备份数量，默认1
                .setMaxSizeConfig(new MaxSizeConfig(1000000, MaxSizeConfig.MaxSizePolicy.PER_NODE)) // 最大数据量，默认int最大值
                .setEvictionPolicy(EvictionPolicy.LRU) // 淘汰策略
                .setTimeToLiveSeconds(604800) // 数据过期时长
                .setMapStoreConfig(testMapStoreConfig) // 数据存储
                ;

        // 可选，网络配置，不配置默认采用广播
        NetworkConfig networkConfig = new NetworkConfig().setJoin(
                new JoinConfig()
                        .setMulticastConfig(new MulticastConfig().setEnabled(false))
                        .setTcpIpConfig(
                                new TcpIpConfig()
                                        .setEnabled(true)
                                        .addMember("127.0.0.1:5701")
                                        .addMember("127.0.0.1.5702")
                                        .addMember("127.0.0.1.5703")
                        ));

        Config config = new Config();
        config.setInstanceName("hazelcast-instance")
                .addMapConfig(mapConfig)
                .setManagementCenterConfig(centerConfig)
                .setNetworkConfig(networkConfig)
                .setProperty("hazelcast.logging.type", "slf4j") // 可选，设置日志框架,jdk：JDK日志记录（默认） log4j、log4j2、slf4j、none：禁用日志记录
        ;

        // 使用计数器功能需配置CPMemberCount(至少为3)
//        config.getCPSubsystemConfig().setCPMemberCount(3);
        return config;
    }
}

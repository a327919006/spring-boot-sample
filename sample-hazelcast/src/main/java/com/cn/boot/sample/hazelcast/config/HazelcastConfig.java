package com.cn.boot.sample.hazelcast.config;

import com.cn.boot.sample.api.model.po.User;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Slf4j
@Configuration
public class HazelcastConfig {

    @Bean
    public HazelcastInstance hzInstance() {
        Config config = new Config();

        // 可选，配置集群名称，默认dev
        GroupConfig groupConfig = new GroupConfig("test");

        // 可选，配置监控中心
        ManagementCenterConfig centerConfig = new ManagementCenterConfig()
                .setEnabled(true)
                .setUrl("http://10.0.0.52:18080/hazelcast-mancenter");

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

        // 可选，网络配置，使用固定ip地址端口方式，不配置默认采用广播
//        NetworkConfig networkConfig = new NetworkConfig().setJoin(
//                new JoinConfig()
//                        .setMulticastConfig(new MulticastConfig().setEnabled(false))
//                        .setTcpIpConfig(
//                                new TcpIpConfig()
//                                        .setEnabled(true)
//                                        .addMember("127.0.0.1:5701")
//                                        .addMember("127.0.0.1.5702")
//                                        .addMember("127.0.0.1.5703")
//                        ));
//        config.setNetworkConfig(networkConfig);


        // 可选，网络配置，使用zk做为注册中心，不配置默认采用广播
//        config.setProperty(GroupProperty.DISCOVERY_SPI_ENABLED.getName(), "true");
//        DiscoveryStrategyConfig discoveryStrategyConfig = new DiscoveryStrategyConfig(new ZookeeperDiscoveryStrategyFactory());
//        discoveryStrategyConfig.addProperty(ZookeeperDiscoveryProperties.ZOOKEEPER_URL.key(), "127.0.0.1:2181");
//        discoveryStrategyConfig.addProperty(ZookeeperDiscoveryProperties.ZOOKEEPER_PATH.key(), "/hazelcast");
//        discoveryStrategyConfig.addProperty(ZookeeperDiscoveryProperties.GROUP.key(), "test");
//
//        DiscoveryConfig discoveryConfig = new DiscoveryConfig();
//        discoveryConfig.addDiscoveryStrategyConfig(discoveryStrategyConfig);
//
//        NetworkConfig networkConfig = new NetworkConfig().setJoin(
//                new JoinConfig()
//                        .setMulticastConfig(new MulticastConfig().setEnabled(false))
//                        .setDiscoveryConfig(discoveryConfig)
//
//        );
//        config.setNetworkConfig(networkConfig);

        config.setInstanceName("hazelcast-instance")
                .setGroupConfig(groupConfig)
                .addMapConfig(mapConfig)
                .setManagementCenterConfig(centerConfig)
                .setProperty("hazelcast.logging.type", "slf4j") // 可选，设置日志框架,jdk：JDK日志记录（默认） log4j、log4j2、slf4j、none：禁用日志记录
        ;

        // 使用计数器、锁等功能需配置CPMemberCount(至少为3，即需启动至少3个Hazelcast节点)
//        config.getCPSubsystemConfig().setCPMemberCount(3);

        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public IMap<String, User> userMap(HazelcastInstance hzInstance) {
        IMap<String, User> userMap = hzInstance.getMap("userMap");
        log.info("userMap.size={}", userMap.size());
        return userMap;
    }

    @Bean
    public MultiMap<String, User> userMultiMap(HazelcastInstance hzInstance) {
        MultiMap<String, User> userMultiMap = hzInstance.getMultiMap("userMultiMap");
        log.info("userMultiMap.size={}", userMultiMap.size());
        return userMultiMap;
    }
}

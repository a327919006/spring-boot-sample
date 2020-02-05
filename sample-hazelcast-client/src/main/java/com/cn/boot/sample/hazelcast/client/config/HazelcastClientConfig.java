package com.cn.boot.sample.hazelcast.client.config;

import com.cn.boot.sample.api.model.po.User;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Set;

/**
 * @author Chen Nan
 */
@Slf4j
@Configuration
public class HazelcastClientConfig {

    /**
     * 方式一：通过代码配置
     */
    @Bean
    public HazelcastInstance hzInstance() {
        ClientConfig config = new ClientConfig();

        // 可选，配置集群名称，默认dev
        config.setGroupConfig(new GroupConfig("test"));

        // 设置缓存服务地址，默认"127.0.0.1:5701", "127.0.0.1:5702", "127.0.0.1:5703"
//        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
//        clientNetworkConfig.addAddress("127.0.0.1:5701", "127.0.0.1:5702");
//        config.setNetworkConfig(clientNetworkConfig);

        // 可选，从zk注册中心获取缓存服务地址
//        config.setProperty(GroupProperty.DISCOVERY_SPI_ENABLED.getName(), "true");
//        DiscoveryStrategyConfig discoveryStrategyConfig = new DiscoveryStrategyConfig(new ZookeeperDiscoveryStrategyFactory());
//        discoveryStrategyConfig.addProperty(ZookeeperDiscoveryProperties.ZOOKEEPER_URL.key(), "127.0.0.1:2181");
//        discoveryStrategyConfig.addProperty(ZookeeperDiscoveryProperties.ZOOKEEPER_PATH.key(), "/hazelcast");
//        discoveryStrategyConfig.addProperty(ZookeeperDiscoveryProperties.GROUP.key(), "test");
//        config.getNetworkConfig().getDiscoveryConfig().addDiscoveryStrategyConfig(discoveryStrategyConfig);

        // 可选，设置上报数据到hazelcast-mancenter
        System.setProperty("hazelcast.client.statistics.enabled", "true");

        // 可选，数据处理器，缓存数据增删改查到数据库
        MapStoreConfig testMapStoreConfig = new MapStoreConfig()
                .setEnabled(true)
                .setClassName("com.cn.boot.sample.hazelcast.loader.TestDataLoader2");
        // 可选，map配置
        MapConfig mapConfig = new MapConfig()
                .setName("test2") // map名称
                .setBackupCount(2) // 数据备份数量，默认1
                .setMaxSizeConfig(new MaxSizeConfig(1000000, MaxSizeConfig.MaxSizePolicy.PER_NODE)) // 最大数据量，默认int最大值
                .setEvictionPolicy(EvictionPolicy.LRU) // 淘汰策略
                .setTimeToLiveSeconds(60) // 数据过期时长
                .setMapStoreConfig(testMapStoreConfig);

        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(config);
        hazelcastInstance.getConfig().addMapConfig(mapConfig);
        return hazelcastInstance;
    }

    /**
     * 方式二：通过xml配置
     */
//    @Bean
//    public HazelcastInstance hazelcastInstanceByXml() {
//        String configFile = "hazelcast-client.xml";
//        ClientConfig hzconfig = new ClientClasspathXmlConfig(configFile);
//        return HazelcastClient.newHazelcastClient(hzconfig);
//    }

    @Bean
    public IMap<String, User> userMap(HazelcastInstance hzInstance) {
        log.info("start");
        IMap<String, User> userMap = hzInstance.getMap("userMap");
        log.info("userMap.size={}", userMap.size());
        User user = userMap.get("1");
        log.info("user={}", user);
        return userMap;
    }

    @Bean
    public MultiMap<String, User> userMultiMap(HazelcastInstance hzInstance) {
        MultiMap<String, User> userMultiMap = hzInstance.getMultiMap("userMultiMap");
        log.info("userMultiMap.size={}", userMultiMap.size());
        return userMultiMap;
    }
}

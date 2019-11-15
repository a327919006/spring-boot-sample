### sample-hazelcast-client
此模块整合Hyzelcast，实现分布式缓存，注意：此模块仅作为缓存客户端，模块本身不作为缓存集群中的节点。
- 官网：https://hazelcast.org/
- 下载：https://hazelcast.org/download/
- 源码：https://github.com/hazelcast/hazelcast

#### 依赖
- https://mvnrepository.com/artifact/com.hazelcast/hazelcast
```
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast-spring</artifactId>
    <version>3.12.3</version>
</dependency>
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast-client</artifactId>
    <version>3.12.3</version>
</dependency>
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast</artifactId>
    <version>3.12.3</version>
</dependency>
```

#### Hazelcast配置
```
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
```

#### 管控台监控（可选）
在java 启动参数中增加-Dhazelcast.client.statistics.enabled=true

#### 使用zookeeper作为注册中心（可选）
- 参考: https://github.com/hazelcast/hazelcast-zookeeper/blob/master/README.md
- 额外依赖：
```$xslt
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast-zookeeper</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-x-discovery</artifactId>
</dependency>
```
- 配置代码：
```$xslt
config.setProperty(GroupProperty.DISCOVERY_SPI_ENABLED.getName(), "true");

DiscoveryStrategyConfig discoveryStrategyConfig = new DiscoveryStrategyConfig(new ZookeeperDiscoveryStrategyFactory());
discoveryStrategyConfig.addProperty(ZookeeperDiscoveryProperties.ZOOKEEPER_URL.key(), "127.0.0.1:2181");
discoveryStrategyConfig.addProperty(ZookeeperDiscoveryProperties.ZOOKEEPER_PATH.key(), "/hazelcast");
discoveryStrategyConfig.addProperty(ZookeeperDiscoveryProperties.GROUP.key(), "test");
config.getNetworkConfig().getDiscoveryConfig().addDiscoveryStrategyConfig(discoveryStrategyConfig);
```
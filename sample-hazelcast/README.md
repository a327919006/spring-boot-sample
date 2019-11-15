### sample-hazelcast
此模块整合Hazelcast，实现分布式缓存
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
```

#### Hazelcast配置
```
@Configuration
public class HazelcastConfig {

    @Bean
    public Config hazelCastConfig() {
        // 配置管控台(可选)
        ManagementCenterConfig centerConfig = new ManagementCenterConfig()
                .setEnabled(true)
                .setUrl("http://localhost:8080/hazelcast-mancenter");

        Config config = new Config();
        config.setInstanceName("hazelcast-instance")
                .setManagementCenterConfig(centerConfig);
        return config;
    }
}
```

#### 使用Hazelcast的Map
示例：MapController
```
    @Autowired
    private HazelcastInstance hazelcastInstance;

    IMap<Object, Object> dataMap = hazelcastInstance.getMap("test");
    // 设置
    dataMap.put(key, value);

    // 获取
    dataMap.get(key);

    // 获取所有
    hazelcastInstance.getMap(map);
```

#### 管控平台（可选）
- 下载管控台压缩包(hazelcast-management-center-3.12.5.zip)
- 解压后运行start.bat(Windows)或start.sh(Linux)
- 访问http://localhost:8080/hazelcast-mancenter
- 首次登录需配置管理账号和密码
```
默认端口:8080，路径:hazelcast-mancenter
如需指定路径或端口执行 start.bat 18080 hazelcast-mancenter
或执行java -jar hazelcast-mancenter-3.12.5.war 18080 hazelcast-mancenter
```

#### 使用计数器AtomicLong
注意：使用此功能需至少启动3个节点，示例：AtomicController
```
    IAtomicLong dataMap = hazelcastInstance.getCPSubsystem().getAtomicLong(name);
    long num1 = dataMap.incrementAndGet();
    long num2 = dataMap.get();
```

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

DiscoveryConfig discoveryConfig = new DiscoveryConfig();
discoveryConfig.addDiscoveryStrategyConfig(discoveryStrategyConfig);

NetworkConfig networkConfig = new NetworkConfig().setJoin(
        new JoinConfig()
                .setMulticastConfig(new MulticastConfig().setEnabled(false))
                .setDiscoveryConfig(discoveryConfig)

);
```
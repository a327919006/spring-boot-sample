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
- 解压后运行start.bat或start.sh或执行java -jar hazelcast-mancenter-3.12.5.war 8080 hazelcast-mancenter
- 访问http://localhost:8080/hazelcast-mancenter
- 首次登录需配置管理账号和密码

#### 使用计数器AtomicLong
注意：使用此功能需至少启动3个节点，示例：AtomicController
```
    IAtomicLong dataMap = hazelcastInstance.getCPSubsystem().getAtomicLong(name);
    long num1 = dataMap.incrementAndGet();
    long num2 = dataMap.get();
```
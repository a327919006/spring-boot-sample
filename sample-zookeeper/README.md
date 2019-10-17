### sample-amqp
此模块用于对接ZooKeeper
包含：
- controller：用于测试的Http接口
- curator：使用Curator框架对接ZooKeeper
- zkclient：使用zkClient框架对接ZooKeeper
- register：应用示例：使用zk实现服务注册、发现，并实现客户端路由接口(根据机器负载、机器标识)

#### 使用zkClient
引入依赖
```
<dependency>
    <groupId>com.101tec</groupId>
    <artifactId>zkclient</artifactId>
    <version>0.11</version>
</dependency>
```

#### 使用curator
引入依赖
```
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>4.0.1</version>
</dependency>
```

#### register模块
需依赖sample-hazelcast项目作为分布式缓存，需先启动该模块:HazelcastApplication

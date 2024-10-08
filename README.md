<p align="center">
    <h1 align="center">spring-boot-sample</h1>
</p>

<p align="center">
    <a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0.html">
        <img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" ></img>
    </a>
    <a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
        <img src="https://img.shields.io/badge/JDK-1.8+-green.svg" ></img>
    </a>
    <a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
        <img src="https://img.shields.io/badge/SpringBoot-2.1+-red.svg" ></img>
    </a>
</p>

------------

#### 介绍

SpringBoot 2.3.x 整合其他框架：MyBatis、Dubbo、Swagger2、SpringSecurity、RabbitMQ、RocketMQ、Kafka、Redis、MongoDB、Hazelcast、Apollo等

如果感觉有帮助，帮忙点个star！

#### Maven模块描述

| 端口 | 模块名称 | 描述 |
| --- | --- | --- |
| 10100         | sample-actuator           | 整合Actuator、Micrometer、Prometheus |
| 10082         | sample-amqp               | 整合RabbitMQ |
| -----         | sample-api                | 服务接口、工具类、实体类 |
| 10087         | sample-apollo             | 整合Apollo配置中心 |
| 10081         | sample-business           | 业务接口，整合Swagger，详情查看该模块README |
| 10106         | sample-cadence            | 整合cadence，实现工作流 |
| 10081         | sample-canal              | 整合canal，实现mysql的binlog日志订阅 |
| -----         | sample-dal                | mapper、sql语句，整合tk-mybatis   |
| -----         | sample-dal-mp             | mapper、sql语句，整合mybatis-plus |
| -----         | sample-design-pattern     | 实现常见设计模式，如单例、工厂、适配器等等 |
| 10099         | sample-es                 | 整合Elasticsearch |
| 10104         | sample-fastdfs            | 整合FastDFS |
| 10090         | sample-geoip2             | 整合GeoIP2,实现根据IP地址获取对应城市 |
| 10095         | sample-guava              | 整合Guava，常用工具类使用示例 |
| 10102         | sample-graphql            | 整合GraphQL，图查询 |
| 10101         | sample-grpc               | 整合Grpc，远程过程调用 |
| 10091         | sample-hazelcast          | 整合hazelcase,分布式缓存 |
| 10094         | sample-hazelcast-client   | 整合hazelcase,此模块只作为缓存客户端，不加入缓存集群 |
| 10107         | sample-iotdb              | 整合IotDB，时序数据库 |
| 10103         | sample-jackson            | 整合Jackson，json序列化 |
| -----         | sample-java-agent         | 自定义java-agent |
| 10088         | sample-kafka              | 整合Kafka，使用框架spring-kafka |
| 10089         | sample-kafka-avro         | 整合Kafka、Avro |
| 10089         | sample-kafka-origin       | 整合Kafka，使用原生kafka-client |
| 20882         | sample-mongo              | 整合MongoDB、SpringDataMongodb |
| -----         | sample-mongo-api          | MongoDB实体类、服务接口 |
| 10108         | sample-mqtt               | 整合MQTT客户端 |
| 10097,50001   | sample-netty              | 整合Netty，实现长连接数据交互 |
| 10097,50001   | sample-netty-mqtt         | 整合Netty+MQTT+ProtoBuf |
| 10097,50001   | sample-netty-protobuf     | 整合Netty+ProtoBuf，实现长连接数据交互 |
| 10110         | sample-pulsar             | 整合Pulsar |
| 10109         | sample-quartz             | 整合Quartz，实现分布式定时任务 |
| 20883,10096   | sample-redis              | 整合Redis、SpringDataRedis和Jedis两种方式 |
| 10092         | sample-rocketmq           | 整合RocketMQ |
| 10083         | sample-security           | 整合SpringSecurity、SpringSession |
| -----         | sample-security-browser   | 浏览器认证相关配置 |
| -----         | sample-security-app       | app或前后端分离项目的认证相关配置 |
| -----         | sample-security-core      | 认证相关通用配置 |
| 20881         | sample-server             | 服务接口实现，整合Dubbo |
| 20881,10084   | sample-server-druid       | 服务接口实现，整合Dubbo、Druid |
| 20881         | sample-server-dynamic     | 服务接口实现，整合Dubbo、Druid，支持动态数据源 |
| 20881         | sample-server-jpa         | 服务接口实现，整合JPA、JdbcTemplate |
| 20881         | sample-server-jpa-dynamic | 服务接口实现，整合JPA，支持动态数据源 |
| 20881         | sample-server-jpa-more    | 服务接口实现，整合JPA、Hikari，对接多数据源 |
| 20881         | sample-server-more        | 服务接口实现，整合MyBatis、Druid，对接多数据源 |
| 10084         | sample-server-mp          | 服务接口实现，整合MyBatisPlus |
| 20881         | sample-server-shard       | 服务接口实现，整合ShardingSphere，分库分表 |
| 10098         | sample-swagger            | 整合Swagger，使用原生方式 |
| 10085         | sample-webflux            | 业务接口，整合webflux |
| 10105         | sample-websocket          | 整合WebSocket |
| 10086         | sample-wechat             | 微信公众号开发，未使用框架，直接调用微信接口 |
| 10086         | sample-wechat-tool        | 微信公众号开发,使用框架：WxJava |
| 10093         | sample-zookeeper          | 整合ZooKeeper，使用ZkClient、Curator |

##### 说明

- 当前已使用端口10081-10110,20881-20883

```
端口为1xxxx的模块对外提供Http接口，并整合了Swagger，方便调试。
端口为2xxxx的模块提供RPC服务，一般用于business模块调用，由business模块提供测试的http接口
无端口的模块，提供给其他模块引用，如：实体类、工具类、配置类等
```

------------

#### sql文件说明

| sql文件 | 描述 |
| --- | --- |
| boot-sample-init.sql | 基础数据库表结构 |
| boot-sample2-init.sql| 测试多数据源时的数据库表结构 |

------------

#### 已整合框架

| 框架 | 描述 |
| --- | --- |
| SpringBoot | SpringBoot |
| SpringMVC| SpringMVC |
| Spring | Spring |
| MyBatis | MyBatis |
| MyBatisPlus | 通用mapper |
| tk-mybatis | 通用mapper |
| Dubbo | RPC |
| UidGenerator | 百度开源的ID生成框架 |
| Swagger2 | 接口文档 |
| RabbitMQ | 消息队列 |
| RocketMQ | 消息队列 |
| Druid | 阿里数据库连接池 |
| Redis | Redis |
| SpringSecurity | 身份认证 |
| SpringSession | 分布式Session |
| SpringWebflux | 反应式WEB框架 |
| SpringDataJpa | jpa |
| SpringDataRedis | Redis操作框架 |
| SpringDataMongodb | MongoDB操作框架 |
| SpringKafka | Kafka操作框架 |
| Apollo | Apollo配置中心 |
| Hazelcast | 分布式缓存 |
| ZooKeeper | 分布式协调服务 |
| Guava | 常用工具类 |
| ShardingSphere | 分库分表 |
| Netty | 长连接数据交互 |
| Elasticsearch | 全文检索 |
| Canal | MySQL binlog日志订阅 |

#### 项目地址

- https://gitee.com/NuLiing/spring-boot-sample
- https://github.com/a327919006/spring-boot-sample


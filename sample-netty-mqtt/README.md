### sample-netty
整合Netty

#### mqtt服务端依赖
```
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
</dependency>
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java</artifactId>
</dependency>
<dependency>
    <groupId>io.moquette</groupId>
    <artifactId>moquette-broker</artifactId>
</dependency>
```

#### mqtt客户端依赖
```
<dependency>
    <groupId>org.fusesource.mqtt-client</groupId>
    <artifactId>mqtt-client</artifactId>
</dependency>
```


#### 启动Netty服务端
运行com.cn.boot.sample.netty.mqtt包下的NettyMqttApplication

#### 启动Netty客户端
运行test下com.cn.boot.sample.netty.mqtt包下的MqttClientTest

#### 访问swagger界面
- http://localhost:10097/sample-netty-mqtt/swagger-ui.html

deviceNo填d001，可通过http接口测试发送数据给客户端及断开客户端连接
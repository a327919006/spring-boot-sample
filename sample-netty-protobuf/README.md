### sample-netty
整合Netty+ProtoBuf

#### 依赖
```
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
</dependency>
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java</artifactId>
</dependency>
```

#### 启动Netty服务端
运行com.cn.boot.sample.netty.proto包下的NettyProtoApplication

#### 启动Netty客户端
运行test下com.cn.boot.sample.netty.proto包下的ProtoBufClientTest

#### 访问swagger界面
- http://localhost:10097/sample-netty-protobuf/swagger-ui.html

deviceNo填d001，可通过http接口测试发送数据给客户端及断开客户端连接
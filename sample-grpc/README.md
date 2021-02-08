### sample-grpc
整合谷歌grpc，已整合protobuf和grpc插件的依赖

#### 运行说明

```$xslt
执行 protobuf:compile
执行 protobuf:compile-custom
说明：执行命令用于生产protobuf对应的java对象和grpc对应的服务接口
执行结果会自动放在模块target文件夹下：
target/generated-sources/protobuf/grpc-java
target/generated-sources/protobuf/java
```
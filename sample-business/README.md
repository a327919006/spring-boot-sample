### sample-business
- 此模块依赖Zookeeper，启动本模块时请先启动Zookeeper
- 此模块包含以下功能

#### 基础功能说明：
- ControllerExceptionHandler：统一异常处理
- HibernateValidator：参数校验
- WebLogAspect：AOP，自动记录请求与响应日志

#### Controller说明：
部分接口依赖sample-server项目(Dubbo服务)

| Controller名称 | 功能描述 |
| --- | --- |
| AsyncController               | 测试异步接口，类似webflux |
| BackOffController             | 测试Spring提供的退避策略 |
| CaffeineController            | 测试Caffeine缓存 |
| ClientController              | 测试Dubbo+mybatis |
| ConfigController              | 测试自定义Properties，从yaml获取配置 |
| DynamicDataSourceController   | 测试动态数据源 |
| FileController                | 测试文件上传、下载 |
| FtpController                 | 测试FtpClient功能 |
| MessageController             | 测试MongoDB服务接口 |
| OrderController               | 测试ShardJDBC-订单管理 |
| RedisController               | 测试Redis |
| RetryController               | 测试接口退避重试 |
| RuntimeController             | 测试使用oshi框架获取系统运行状态，CPU、内存等  |
| StudentController             | 测试JPA |
| TeacherController             | 测试多数据源 |

#### RuntimeController说明
使用oshi框架获取系统运行状态，CPU、内存等 
- 官网示例代码： https://github.com/oshi/oshi/blob/master/oshi-core/src/test/java/oshi/SystemInfoTest.java
- 引入依赖
```
<dependency>
    <groupId>com.github.oshi</groupId>
    <artifactId>oshi-core</artifactId>
    <version>4.0.0</version>
</dependency>
<dependency>
    <groupId>net.java.dev.jna</groupId>
    <artifactId>jna</artifactId>
    <version>5.4.0</version>
</dependency>
<dependency>
    <groupId>net.java.dev.jna</groupId>
    <artifactId>jna-platform</artifactId>
    <version>5.4.0</version>
</dependency>
```
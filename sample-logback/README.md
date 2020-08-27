### sample-logback
logback打印的业务日志增加traceId，便于排查问题。
整合第三方框架支持跨线程的traceId传递。

#### 依赖
```
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>transmittable-thread-local</artifactId>
</dependency>
<dependency>
    <groupId>com.ofpay</groupId>
    <artifactId>logback-mdc-ttl</artifactId>
</dependency>
```

#### 增加logback配置
```
<contextListener class="com.ofpay.logback.TtlMdcListener"/>
```

#### 增加vm参数
```
-javaagent:\xxxxx\transmittable-thread-local-2.11.5.jar
```

#### 访问swagger界面进行测试
- http://localhost:10099/sample-logback/swagger-ui.html
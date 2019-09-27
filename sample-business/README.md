### sample-business
此模块包含一下功能

#### 基础功能说明：
- ControllerExceptionHandler：统一异常处理
- HibernateValidator：参数校验
- WebLogAspect：AOP，自动记录请求与响应日志

#### Controller说明：
- AsyncController：异步接口，类似webflux
- ClientController：测试Dubbo+mybatis
- ConfigController：自定义Properties，从yaml获取配置
- MessageController：测试MongoDB服务接口
- RedisController：测试Redis
- RuntimeController：获取系统运行状态，CPU、内存等
- StudentController：测试JPA
- TeacherController：测试多数据源


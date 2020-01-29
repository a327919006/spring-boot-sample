### sample-swagger
整合Swagger

#### 依赖
```
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
</dependency>
<!-- 可选，第三方封装的一个更友好的swagger界面,与原生兼容 -->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>swagger-bootstrap-ui</artifactId>
</dependency>
```

#### 配置
```
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("sample-swagger")
                .select()   // 选择那些路径和api会生成document
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("sample-swagger")
                .description("sample-swagger")
                .version("1.0").build();
    }
}

```

#### 使用
示例：TestController
```
@RestController
@RequestMapping("/test")
@Api(tags = "测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {


    @ApiOperation("获取")
    @PostMapping("/getName")
    public Map<String, Object> get(@Valid @RequestBody TestReq req) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("name", req.getName());
        return map;
    }
}

```

#### 界面
- http://127.0.0.1:10098/sample-swagger/swagger-ui.html

或者

- http://127.0.0.1:10098/sample-swagger/doc.html
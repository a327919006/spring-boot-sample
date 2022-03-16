package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.business.config.properties.TestConfigProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/config")
@Api(tags = "测试06-config-加载yaml中自定义配置", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfigController {

    @Autowired
    private TestConfigProperties configProperties;
    @Autowired
    private ConfigurableApplicationContext context;

    @ApiOperation("查询-测试配置")
    @GetMapping("test")
    public TestConfigProperties getTestConfig() {
        return configProperties;
    }

    @ApiOperation("查询-context配置")
    @GetMapping("context")
    public String getContextConfig() {
        // context中可以取到application.yaml中配置的信息
        ConfigurableEnvironment environment = context.getEnvironment();
        return environment.getProperty("test.config.address");
    }
}

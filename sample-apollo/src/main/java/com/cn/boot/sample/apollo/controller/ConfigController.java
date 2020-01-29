package com.cn.boot.sample.apollo.controller;

import com.cn.boot.sample.api.service.ClientService;
import com.cn.boot.sample.apollo.config.properties.Test2ConfigProperties;
import com.cn.boot.sample.apollo.config.properties.TestConfigProperties;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/config")
@Api(tags = "配置管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfigController {

    @Value("${test.config.name}")
    private String name;
    @Autowired
    private TestConfigProperties testProperties;
    @Autowired
    private Test2ConfigProperties test2Properties;

    @ApolloConfig
    private Config config;

    @ApolloConfig("test2.yaml")
    private Config cntest2Config;

    @ApiOperation("配置-获取")
    @GetMapping
    public TestConfigProperties get1() {
        log.info("name = {}", name);
        return testProperties;
    }

    @ApiOperation("配置-获取2")
    @GetMapping("/2")
    public Test2ConfigProperties get2() {
        return test2Properties;
    }

    @ApiOperation("配置-获取默认命名空间配置")
    @GetMapping("/{key}")
    public String getConfig(@PathVariable String key) {
        return config.getProperty(key, "");
    }

    @ApiOperation("配置-获取cntest2命名空间配置")
    @GetMapping("/cntest2/{key}")
    public String getTest2Config(@PathVariable String key) {
        return cntest2Config.getProperty(key, "");
    }
}

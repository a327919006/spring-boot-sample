package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.business.config.properties.TestConfigProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "配置管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfigController {

    @Autowired
    private TestConfigProperties configProperties;

    @ApiOperation("配置-查询")
    @GetMapping
    public TestConfigProperties get() {
        return configProperties;
    }
}

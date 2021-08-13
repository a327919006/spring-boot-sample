package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.business.anotation.ApiVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/multiversion")
@Api(tags = "测试多版本接口", produces = MediaType.APPLICATION_JSON_VALUE)
public class MultiVersionController {

    @ApiOperation("测试默认")
    @GetMapping
    public String test() {
        return "test-default";
    }

    @ApiVersion("1.0")
    @GetMapping
    public String test1() {
        return "test-1.0";
    }

    @ApiVersion("2.0")
    @GetMapping
    public String test2() {
        return "test-2.0";
    }
}

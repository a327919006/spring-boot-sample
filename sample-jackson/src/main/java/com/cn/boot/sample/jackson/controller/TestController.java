package com.cn.boot.sample.jackson.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/test")
@Api(tags = "jackson测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {


    @ApiOperation("获取json数据")
    @GetMapping("str")
    public String getStr() {
        return "aaaa";
    }

    @ApiOperation("获取json数据")
    @GetMapping("str/base")
    public RspBase<String> getStrRsp() {
        return RspBase.data("123");
    }
}

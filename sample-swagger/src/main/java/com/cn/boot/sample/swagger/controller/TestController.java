package com.cn.boot.sample.swagger.controller;

import com.cn.boot.sample.swagger.model.TestReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
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

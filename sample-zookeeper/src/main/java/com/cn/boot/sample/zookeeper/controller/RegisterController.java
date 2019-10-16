package com.cn.boot.sample.zookeeper.controller;

import com.cn.boot.sample.zookeeper.register.RegisterUtil;
import com.cn.boot.sample.zookeeper.register.properties.ServerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/register")
@Api(tags = "注册中心测试", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RegisterController {

    @Autowired
    private ServerConfig config;
    @Autowired
    private RegisterUtil registerUtil;

    @ApiOperation("获取配置")
    @GetMapping("/config")
    public ServerConfig getConfig() {
        return config;
    }

    @ApiOperation("获取在线节点")
    @GetMapping("/nodes")
    public Set<String> getNodeSet() {
        return registerUtil.getNodeSet();
    }
}

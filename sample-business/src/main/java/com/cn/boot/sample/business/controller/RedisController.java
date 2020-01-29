package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.exceptions.BusinessException;
import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.ClientService;
import com.cn.boot.sample.api.service.RedisService;
import com.cn.boot.sample.api.service.UidGeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
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
@RequestMapping("/redis")
@Api(tags = "Redis测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class RedisController {

    @Reference
    private RedisService redisService;
    @Reference
    private ClientService clientService;
    @Reference
    private UidGeneratorService uidGeneratorService;

    @ApiOperation("锁")
    @GetMapping("/set/{key}")
    public String set(@PathVariable String key) {
        String value = uidGeneratorService.generate();
        redisService.lock(key, value);
        return value;
    }

    @ApiOperation("缓存对象")
    @GetMapping("/client/cache/{id}")
    public Client cache(@PathVariable String id) {
        Client client = clientService.selectByPrimaryKey(id);
        if (client == null) {
            throw new BusinessException("商户不存在");
        }

        redisService.cache(id, client);
        return client;
    }

    @ApiOperation("获取对象")
    @GetMapping("/{key}")
    public Object get(@PathVariable String key) {
        return redisService.get(key);
    }

    @ApiOperation("获取商户")
    @GetMapping("/client/{id}")
    public Client getClient(@PathVariable String id) {
        return redisService.getClient(id);
    }
}

package com.cn.boot.sample.redis.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/common/test")
@Api(tags = "基础功能测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonTestController {

    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private RAtomicLong atomicLong;

    @Value("${spring.redis.database}")
    private int database;

    @ApiOperation("1、hgetAll命令测试，默认返回空map")
    @PostMapping("/hgetAll")
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = jedisPool.getResource();
        return jedis.hgetAll(key);
    }

    @ApiOperation("2、测试redisson，AtomicLong")
    @GetMapping("/atomic/addAndGet")
    public String atomic(long expire) {
        String num = String.valueOf(atomicLong.addAndGet(1));
        atomicLong.expire(expire, TimeUnit.MILLISECONDS);
        return StrUtil.fillBefore(num, '0', 4);
    }


}

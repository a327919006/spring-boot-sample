package com.cn.boot.sample.redis.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.cn.boot.sample.api.model.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/pressure/test")
@Api(tags = "压力测试", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PressureTestController {

    @Autowired
    private JedisPool jedisPool;

    @Value("${spring.redis.database}")
    private int database;

    /**
     * kv 写入速率2W/s 4G内存 最大2500w左右个数据(key平均35位，value32位)
     *
     * @param dataCount 写入数据数量
     * @param printStep 打印日志步长
     */
    @ApiOperation("kv 写入测试")
    @PostMapping("/kv")
    public String kv(int dataCount, int printStep) {
        long start = System.currentTimeMillis();
        long temp = System.currentTimeMillis();
        Jedis jedis = jedisPool.getResource();
        jedis.select(database);

        String data = RandomUtil.randomNumbers(32);
        String key = IdUtil.simpleUUID();
        log.info("key = {}", key);
        for (int i = 0; i < dataCount; i++) {
            jedis.set(key + i, data);
            if (0 == i % printStep) {
                log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                temp = System.currentTimeMillis();
            }
        }
        log.info("总耗时:{}", System.currentTimeMillis() - start);
        jedis.close();
        return Constants.MSG_SUCCESS;
    }

    /**
     * kv 写入速率9W/s 8G内存 最大3000w左右个数据(key平均35位，value32位)
     *
     * @param dataCount 写入数据数量
     * @param printStep 打印日志步长
     */
    @ApiOperation("hash 写入测试")
    @PostMapping("/hash")
    public String hmset(int dataCount, int printStep) {
        long start = System.currentTimeMillis();
        long temp = System.currentTimeMillis();
        Jedis jedis = jedisPool.getResource();
        jedis.select(database);

        String data = RandomUtil.randomNumbers(32);
        String key = IdUtil.simpleUUID();
        Map<String, String> map = new HashMap<>();
        log.info("key = {}", key);
        for (int i = 0; i < dataCount; i++) {
            map.clear();
            map.put(key + i, data);
            jedis.hmset("HashPressureTest", map);
            if (0 == i % printStep) {
                log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                temp = System.currentTimeMillis();
            }
        }
        log.info("总耗时:{}", System.currentTimeMillis() - start);
        jedis.close();
        return Constants.MSG_SUCCESS;
    }
}

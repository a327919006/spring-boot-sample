package com.cn.boot.sample.redis.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/redisson/test")
@Api(tags = "4、Redisson功能测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class RedissonTestController {

    @Autowired
    private Redisson redisson;
    @Autowired
    private RAtomicLong atomicLong;
    @Autowired
    private RBloomFilter<Long> bloomFilter;

    @ApiOperation("1、AtomicLong")
    @GetMapping("/atomic")
    public String atomic(long expire) {
        String num = String.valueOf(atomicLong.addAndGet(1));
        atomicLong.expire(expire, TimeUnit.MILLISECONDS);
        return StrUtil.fillBefore(num, '0', 4);
    }

    /**
     * redisson提供的布隆过滤器是基于jvm内存的，非redis
     * @param orderId
     * @return
     */
    @ApiOperation("2、BloomFilter-添加订单")
    @GetMapping("/bloomFilter/add")
    public boolean bloomFilterAdd(Long orderId) {
        // 结果true表示插入成功，false表示已存在
        return bloomFilter.add(orderId);
    }

    @ApiOperation("3、BloomFilter-查询订单")
    @GetMapping("/bloomFilter/contains")
    public boolean bloomFilterContains(Long orderId) {
        // 结果true表示插入成功，false表示已存在
        long count = bloomFilter.count();
        long size = bloomFilter.getSize();
        // count返回当前
        log.info("count={} site={}", count, size);
        return bloomFilter.contains(orderId);
    }
}

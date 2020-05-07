package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.Constants;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/caffeine")
@Api(tags = "咖啡因缓存", produces = MediaType.APPLICATION_JSON_VALUE)
public class CaffeineController {
    private static final Cache<String, List<String>> cache = Caffeine.newBuilder()
            .initialCapacity(2000)//初始大小
            .maximumSize(2000)//最大数量
            .expireAfterWrite(7, TimeUnit.DAYS)//过期时间
            .build();

    @ApiOperation("添加元素")
    @PostMapping
    public String put(String key, String value) {
        List<String> values = cache.getIfPresent(key);
        if (values == null) {
            values = new ArrayList<>();
        }
        values.add(value);
        cache.put(key, values);

        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("获取")
    @GetMapping
    public List<String> get(String key) {
        return cache.getIfPresent(key);
    }

    @ApiOperation("删除元素")
    @DeleteMapping
    public String del(String key, String value) {
        List<String> values = cache.getIfPresent(key);
        if (values == null) {
            values = new ArrayList<>();
        }
        values.remove(value);

        return Constants.MSG_SUCCESS;
    }
}

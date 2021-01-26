package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.Constants;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/caffeine")
@Api(tags = "咖啡因缓存", produces = MediaType.APPLICATION_JSON_VALUE)
public class CaffeineController {
    private static Cache<String, List<String>> cache = Caffeine.newBuilder()
            .initialCapacity(2000)//初始大小
            .maximumSize(2000)//最大数量
            .expireAfterWrite(7, TimeUnit.DAYS)//过期时间
            .build();

    /**
     * LoadingCache可以配置自动回源的方法，在key不存在的情况下，自动回源查询
     */
    private static LoadingCache<String, String> loadCache = Caffeine.newBuilder()
            .initialCapacity(2000)//初始大小
            .maximumSize(2000)//最大数量
            .expireAfterWrite(7, TimeUnit.DAYS)//过期时间
            .build(key -> getDataFromDb(key));

    private static Cache<String, String> listenerCache = Caffeine.newBuilder()
            .expireAfterWrite(2000, TimeUnit.MILLISECONDS)
            .removalListener((RemovalListener<String, String>) (key, value, cause) -> {
                log.info("onRemoval key={} value={} cause={}", key, value, cause.name());
            })
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


    @ApiOperation("添加元素")
    @PostMapping("/loadcache/put")
    public String loadCachePut(String key, String value) {
        loadCache.put(key, value);

        return Constants.MSG_SUCCESS;
    }

    /**
     * 批量获取，如果所有key都不存在，则返回空Map
     */
    @ApiOperation("获取")
    @GetMapping("/loadcache/getall")
    public Map<String, String> loadCacheGetAll() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("3");
        list.add("4");
        return loadCache.getAll(list);
    }

    @ApiOperation("添加元素")
    @PostMapping("/listenercache/put")
    public String listenerCachePut(String key, String value) {
        listenerCache.put(key, value);

        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("获取")
    @GetMapping("/listenercache/get/{key}")
    public String listenerCacheGet(@PathVariable String key) {
        return listenerCache.getIfPresent(key);

//        for (int i = 0; i < 10000000; i++) {
//            listenerCache.getIfPresent(key);
//            ThreadUtil.sleep(100);
//        }
//        return Constants.MSG_SUCCESS;
    }

    /**
     * 模拟数据回源查询
     */
    private static String getDataFromDb(String key) {
        log.info("getDataFromDb");
        return key + 111;
    }
}

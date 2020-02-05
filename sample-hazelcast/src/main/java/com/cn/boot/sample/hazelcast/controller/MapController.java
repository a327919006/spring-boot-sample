package com.cn.boot.sample.hazelcast.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.model.po.User;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/map")
@Api(tags = "MAP", produces = MediaType.APPLICATION_JSON_VALUE)
public class MapController {

    @Autowired
    private HazelcastInstance hzInstance;

    @ApiOperation("保存数据")
    @PostMapping("/{map}")
    public String set(@PathVariable String map, @RequestParam String key, @RequestParam String value, Long ttl) {
        if (ttl == null) {
            ttl = 0L;
        }
        IMap<String, String> dataMap = hzInstance.getMap(map);
        dataMap.put(key, value, ttl, TimeUnit.MILLISECONDS);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("获取")
    @GetMapping("/{map}")
    public String get(@PathVariable String map, @RequestParam String key) {
        IMap<String, String> dataMap = hzInstance.getMap(map);
        return dataMap.get(key);
    }

    @ApiOperation("所有")
    @GetMapping("/{map}/all")
    public IMap<Object, Object> all(@PathVariable String map) {
        return hzInstance.getMap(map);
    }

    @ApiOperation("删除")
    @DeleteMapping("/{map}")
    public String remove(@PathVariable String map, @RequestParam String key) {
        IMap<String, String> dataMap = hzInstance.getMap(map);
        return dataMap.remove(key);
    }

    @ApiOperation("删除-指定值")
    @DeleteMapping("/{map}/kv")
    public boolean removeValue(@PathVariable String map, @RequestParam String key, @RequestParam String value) {
        IMap<String, String> dataMap = hzInstance.getMap(map);
        return dataMap.remove(key, value);
    }

    @ApiOperation(value = "测试-值为Map", notes = "测试结果：支持值为map")
    @GetMapping("/test/map")
    public Map<String, User> testMap() {
        String mapName = "testMap10";
        String key = "test";
        IMap<String, Map<String, User>> dataMap = hzInstance.getMap(mapName);
        Map<String, User> data = new HashMap<>();
        data.put("k1", new User().setUsername("u1"));
        data.put("k2", new User().setUsername("u2"));
        data.put("k3", new User().setUsername("u3"));
        dataMap.put(key, data);

        Map<String, User> testData = dataMap.get(key);
        testData.remove("k2");

        dataMap.put(key, testData);
        return testData;
    }

    @ApiOperation(value = "测试-默认值是否为null", notes = "测试结果：默认key不存在时，值返回null")
    @GetMapping("/test/other")
    public String testOther() {
        String mapName = "test11";
        String key = "key";
        IMap<String, Integer> dataMap = hzInstance.getMap(mapName);
        Integer data = dataMap.get(key);
        if (data == null) {
            log.info("data null");

            dataMap.put(key, 1);
            data = dataMap.get(key);
            log.info("data1 = {}", data);

            dataMap.delete(key);
            data = dataMap.get(key);
            log.info("data2 = {}", data);
        } else {
            log.info("data = {}", data);
        }
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation(value = "测试-put和set的区别", notes = "测试结果：put返回旧值，set不返回，set效率高")
    @GetMapping("/test/put")
    public String testPut() {
        String mapName = "test12";
        String key = "key";
        IMap<String, Integer> dataMap = hzInstance.getMap(mapName);
        log.info("put1 = {}", dataMap.put(key, 1));
        log.info("put2 = {}", dataMap.put(key, 2));
        dataMap.set(key, 3);
        log.info("put3 = {}", dataMap.put(key, 4));
        return Constants.MSG_SUCCESS;
    }


    @ApiOperation(value = "测试-remove和delete的区别", notes = "测试结果：remove返回旧值，delete不返回，delete效率高")
    @GetMapping("/test/remove")
    public String testRemove() {
        String mapName = "test13";
        String key = "key";
        IMap<String, Integer> dataMap = hzInstance.getMap(mapName);
        dataMap.set(key, 1);
        Integer oldValue = dataMap.remove(key);
        log.info("oldValue = {}", oldValue);

        dataMap.set(key, 1);
        dataMap.delete(key);
        return Constants.MSG_SUCCESS;
    }
}

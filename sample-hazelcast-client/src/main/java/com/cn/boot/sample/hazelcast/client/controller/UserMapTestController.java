package com.cn.boot.sample.hazelcast.client.controller;

import cn.hutool.core.util.IdUtil;
import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.model.po.User;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "UserMapTest", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserMapTestController {

    @Autowired
    private HazelcastInstance hzInstance;
    @Autowired
    private IMap<String, User> userMap;
    @Autowired
    private MultiMap<String, User> userMultiMap;

    @ApiOperation("保存数据")
    @PostMapping("map")
    public String setMap(@RequestParam String key) {
        User user = new User();
        user.setId(key);
        userMap.set(key, user);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("测试getMap")
    @GetMapping("map0")
    public User getMap0(@RequestParam String key) {
        IMap<Object, Object> userMap1 = hzInstance.getMap("userMap");
        IMap<Object, Object> userMap2 = hzInstance.getMap("userMap");

        log.info("userMap1.size={}", userMap1.size());
        log.info("userMap2.size={}", userMap2.size());
        return this.userMap.get(key);
    }

    @ApiOperation("测试keySet")
    @GetMapping("map1")
    public User getMap1(@RequestParam String key) {
        Set<String> keySet1 = userMap.keySet();
        Set<String> keySet2 = userMap.keySet();
        log.info("keySet.size={}", keySet1.size());
        log.info("keySet.size={}", keySet2.size());

        return userMap.get(key);
    }

    @ApiOperation("测试获取全量数据")
    @GetMapping("map2")
    public User getMap2(@RequestParam String key) {
        Set<String> keySet = userMap.keySet();
        Collection<User> values = userMap.values();
        log.info("keySet.size={}", keySet.size());
        log.info("values.size={}", values.size());

        return userMap.get(key);
    }

    @ApiOperation("测试获取单个数据")
    @GetMapping("map3")
    public User getMap3(@RequestParam String key) {
        return this.userMap.get(key);
    }

    @ApiOperation("添加Map测试数据")
    @PostMapping("map/data")
    public String addMapData(int count) {
        User user = new User();
        for (int i = 0; i < count; i++) {
            String key = IdUtil.simpleUUID();
            user.setId(key);
            userMap.set(key, user);
        }
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("保存数据")
    @PostMapping("multimap")
    public String setMultiMap(@RequestParam String key) {
        User user = new User();
        user.setId(key);
        userMultiMap.put(key, user);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("获取")
    @GetMapping("multimap")
    public Collection<User> getMultiMap(@RequestParam String key) {
        return userMultiMap.get(key);
    }

    @ApiOperation("添加MultiMap测试数据")
    @PostMapping("multimap/data")
    public String addMultiMapData(int count) {
        User user = new User();
        for (int i = 0; i < count; i++) {
            String key = IdUtil.simpleUUID();
            user.setId(key);
            userMultiMap.put(key, user);
        }
        return Constants.MSG_SUCCESS;
    }
}

package com.cn.boot.sample.hazelcast.controller;

import cn.hutool.core.util.IdUtil;
import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.model.po.User;
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

    @ApiOperation("获取")
    @GetMapping("map")
    public User getMap(@RequestParam String key) {
        Set<String> keySet = userMap.keySet();
        log.info("keySet.size={}", keySet.size());

        return userMap.get(key);
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

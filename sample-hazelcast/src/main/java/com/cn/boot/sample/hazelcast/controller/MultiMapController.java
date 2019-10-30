package com.cn.boot.sample.hazelcast.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.model.po.User;
import com.hazelcast.core.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/multimap")
@Api(tags = "MultiMap", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MultiMapController {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @ApiOperation("保存数据")
    @PostMapping("/{map}")
    public String set(@PathVariable String map, @RequestParam String key, @RequestParam String value) {
        MultiMap<Object, Object> multiMap = hazelcastInstance.getMultiMap(map);
        multiMap.put(key, value);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("获取")
    @GetMapping("/{map}")
    public Collection<Object> get(@PathVariable String map, @RequestParam String key) {
        MultiMap<Object, Object> multiMap = hazelcastInstance.getMultiMap(map);
        return multiMap.get(key);
    }

    @ApiOperation("所有")
    @GetMapping("/{map}/all")
    public MultiMap<Object, Object> all(@PathVariable String map) {
        return hazelcastInstance.getMultiMap(map);
    }
}

package com.cn.boot.sample.hazelcast.client.controller;

import com.cn.boot.sample.api.model.Constants;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public String set(@PathVariable String map, @RequestParam String key, @RequestParam String value) {
        IMap<Object, Object> dataMap = hzInstance.getMap(map);
        dataMap.put(key, value);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("获取")
    @GetMapping("/{map}")
    public Object get(@PathVariable String map, @RequestParam String key) {
        IMap<Object, Object> dataMap = hzInstance.getMap(map);
        return dataMap.get(key);
    }

    @ApiOperation("所有")
    @GetMapping("/{map}/all")
    public IMap<Object, Object> all(@PathVariable String map) {
        return hzInstance.getMap(map);
    }

}

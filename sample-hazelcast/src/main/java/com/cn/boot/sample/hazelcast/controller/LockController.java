package com.cn.boot.sample.hazelcast.controller;

import com.cn.boot.sample.api.model.Constants;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.cp.lock.FencedLock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * FencedLock，需在HazelcastConfig中配置config.getCPSubsystemConfig().setCPMemberCount(3);
 * 并启动3个节点
 *
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/lock")
@Api(tags = "锁", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LockController {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @ApiOperation("map加锁")
    @PostMapping("/{map}/{key}")
    public String mapLock(@PathVariable String map, @PathVariable String key, Long ttl) {
        if (ttl == null) {
            ttl = 30L;
        }
        IMap<Object, Object> dataMap = hazelcastInstance.getMap(map);
        dataMap.lock(key, ttl, TimeUnit.SECONDS);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("map释放锁")
    @DeleteMapping("/{map}/{key}")
    public String mapUnlock(@PathVariable String map, @PathVariable String key) {
        IMap<Object, Object> dataMap = hazelcastInstance.getMap(map);
        dataMap.unlock(key);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("map检查锁")
    @GetMapping("/{map}/{key}")
    public boolean mapIsLock(@PathVariable String map, @PathVariable String key) {
        IMap<Object, Object> dataMap = hazelcastInstance.getMap(map);
        return dataMap.isLocked(key);
    }



    @ApiOperation("加锁")
    @PostMapping("/{name}")
    public long lock(@PathVariable String name) {
        FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(name);
        return lock.lockAndGetFence();
    }

    @ApiOperation("释放锁")
    @DeleteMapping("/{name}")
    public String unlock(@PathVariable String name) {
        FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(name);
        lock.unlock();
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("检查锁")
    @GetMapping("/{name}")
    public boolean isLock(@PathVariable String name) {
        FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(name);
        return lock.isLocked();
    }

}

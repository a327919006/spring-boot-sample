package com.cn.boot.sample.hazelcast.controller;

import cn.hutool.core.thread.ThreadUtil;
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
@Api(tags = "锁", produces = MediaType.APPLICATION_JSON_VALUE)
public class LockController {

    @Autowired
    private HazelcastInstance hzInstance;

    @ApiOperation("map.tryLock加锁")
    @PostMapping("/{map}/{key}/try")
    public String mapTryLock(@PathVariable String map, @PathVariable String key, @RequestParam String value, Long ttl) throws InterruptedException {
        if (ttl == null) {
            ttl = 30L;
        }
        IMap<Object, Object> dataMap = hzInstance.getMap(map);
        boolean result = dataMap.tryLock(key, 0, TimeUnit.MICROSECONDS, ttl, TimeUnit.SECONDS);
        if (result) {
            try {
                log.info("获取锁{}成功，开始处理业务", key);
                ThreadUtil.sleep(5000);

                // 模拟异常
                if ("0".equals(value)) {
                    int i = 1 / 0;
                }

                log.info("业务处理完成");
                return Constants.MSG_SUCCESS;
            } finally {
                dataMap.unlock(key);
                log.info("释放锁{}成功", key);
            }
        } else {
            log.info("获取锁{}失败", key);
            return Constants.MSG_FAIL;
        }
    }

    @ApiOperation("map.lock加锁")
    @PostMapping("/{map}/{key}")
    public String mapLock(@PathVariable String map, @PathVariable String key, @RequestParam String value, Long ttl) throws InterruptedException {
        if (ttl == null) {
            ttl = 30L;
        }
        IMap<Object, Object> dataMap = hzInstance.getMap(map);
        dataMap.lock(key, ttl, TimeUnit.SECONDS);
        try {
            log.info("获取锁{}成功，开始处理业务", key);
            ThreadUtil.sleep(5000);

            // 模拟异常
            if ("0".equals(value)) {
                int i = 1 / 0;
            }

            log.info("业务处理完成");
            return Constants.MSG_SUCCESS;
        } finally {
            dataMap.unlock(key);
            log.info("释放锁{}成功", key);
        }
    }

    @ApiOperation(value = "map释放锁", notes = "此方法会抛出异常，仅允许上锁线程释放锁!")
    @DeleteMapping("/{map}/{key}")
    public String mapUnlock(@PathVariable String map, @PathVariable String key) {
        IMap<Object, Object> dataMap = hzInstance.getMap(map);
        dataMap.unlock(key);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("map检查锁")
    @GetMapping("/{map}/{key}")
    public boolean mapIsLock(@PathVariable String map, @PathVariable String key) {
        IMap<Object, Object> dataMap = hzInstance.getMap(map);
        return dataMap.isLocked(key);
    }


    @ApiOperation("加锁")
    @PostMapping("/{name}")
    public long lock(@PathVariable String name) {
        FencedLock lock = hzInstance.getCPSubsystem().getLock(name);
        return lock.lockAndGetFence();
    }

    @ApiOperation("释放锁")
    @DeleteMapping("/{name}")
    public String unlock(@PathVariable String name) {
        FencedLock lock = hzInstance.getCPSubsystem().getLock(name);
        lock.unlock();
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("检查锁")
    @GetMapping("/{name}")
    public boolean isLock(@PathVariable String name) {
        FencedLock lock = hzInstance.getCPSubsystem().getLock(name);
        return lock.isLocked();
    }

}

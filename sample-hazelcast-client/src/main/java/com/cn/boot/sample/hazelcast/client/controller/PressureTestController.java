package com.cn.boot.sample.hazelcast.client.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.model.po.User;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.TreeMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/pressure/test")
@Api(tags = "压力测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class PressureTestController {

    @Autowired
    private HazelcastInstance hzInstance;

    /**
     * 写入速率9W/s 8G内存 最大3000w左右个数据(key平均35位，value32位)
     *
     */
    @ApiOperation("1、MAP<K,Object>写入测试")
    @PostMapping("/map/object")
    public String addMapObject(int threadCount, int total, int printStep) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int count = total / threadCount;

        for(int j = 0; j < threadCount; j++){
            singleThreadPool.execute(()->{
                long start = System.currentTimeMillis();
                long temp = System.currentTimeMillis();
                String mapName = "mapObject";
                String key = IdUtil.simpleUUID();
                log.info("mapName = {}, key = {}", mapName, key);
                String data = RandomUtil.randomNumbers(75);
                IMap<String, User> dataMap = hzInstance.getMap(mapName);
                User user = new User();
                for (int i = 0; i < count; i++) {
                    user.setId(key + i);
                    user.setUsername(data);
                    dataMap.set(key + i, user);
                    if (0 == i % printStep) {
                        log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                        temp = System.currentTimeMillis();
                    }
                }
                log.info("总耗时:{}", System.currentTimeMillis() - start);
            });
        }
        return Constants.MSG_SUCCESS;
    }

    /**
     *
     */
    @ApiOperation("2、MAP<K,Object>读取测试")
    @GetMapping("/map/object")
    public String getMapObject(String key, int count, int printStep) {
        long start = System.currentTimeMillis();
        long temp = System.currentTimeMillis();
        String mapName = "mapObject";
        log.info("mapName = {}, key = {}", mapName, key);
        IMap<String, User> dataMap = hzInstance.getMap(mapName);
        for (int i = 0; i < count; i++) {
            dataMap.get(key);
            if (0 == i % printStep) {
                log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                temp = System.currentTimeMillis();
            }
        }
        log.info("总耗时:{}", System.currentTimeMillis() - start);
        return Constants.MSG_SUCCESS;
    }

    /**
     *
     * @param dataCount 写入数据数量
     * @param printStep 打印日志步长
     */
    @ApiOperation("3、MultiMap<K,Object>写入测试")
    @PostMapping("/multimap/object")
    public String addMultimapObject(int dataCount, int printStep) {
        long start = System.currentTimeMillis();
        long temp = System.currentTimeMillis();
        String mapName = "multimapObject";
        String key = IdUtil.simpleUUID();
        log.info("mapName = {}, key = {}", mapName, key);
        String data = RandomUtil.randomNumbers(70);
        MultiMap<String, User> dataMap = hzInstance.getMultiMap(mapName);
        User user = new User();
        for (int i = 0; i < dataCount; i++) {
            user.setId(key + i);
            user.setUsername(data);
            dataMap.put(key + i, user);
            if (0 == i % printStep) {
                log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                temp = System.currentTimeMillis();
            }
        }
        log.info("总耗时:{}", System.currentTimeMillis() - start);
        return Constants.MSG_SUCCESS;
    }

    /**
     *
     */
    @ApiOperation("4、MultiMap<K,Object>读取测试")
    @GetMapping("/multimap/object")
    public String getMultimapObject(String key, int count, int printStep) {
        long start = System.currentTimeMillis();
        long temp = System.currentTimeMillis();
        String mapName = "multimapObject";
        log.info("mapName = {}, key = {}", mapName, key);
        MultiMap<String, User> dataMap = hzInstance.getMultiMap(mapName);
        for (int i = 0; i < count; i++) {
            dataMap.get(key);
            if (0 == i % printStep) {
                log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                temp = System.currentTimeMillis();
            }
        }
        log.info("总耗时:{}", System.currentTimeMillis() - start);
        return Constants.MSG_SUCCESS;
    }

    /**
     *
     * @param dataCount 写入数据数量
     * @param printStep 打印日志步长
     */
    @ApiOperation("5、MAP<K,TreeMap<K,V>>写入测试,加锁")
    @PostMapping("/map/treemap/lock")
    public String addMapTreeMap(int dataCount, int printStep) {
        long start = System.currentTimeMillis();
        long temp = System.currentTimeMillis();
        String mapName = "mapTreeMap";
        String key = "g1";
        log.info("mapName = {}, key = {}", mapName, key);
        IMap<String, TreeMap<Long, Long>> dataMap = hzInstance.getMap(mapName);
        AtomicLong atomicLong = new AtomicLong(System.currentTimeMillis());
        for (int i = 0; i < dataCount; i++) {
            dataMap.lock(key);
            try {
                TreeMap<Long, Long> treeMap = dataMap.get(key);
                long value = atomicLong.getAndIncrement();
                if (null == treeMap) {
                    treeMap = new TreeMap<>();
                    treeMap.put(value, value);
                } else {
                    treeMap.put(value, value);
                    if(100 < treeMap.size()){
                        treeMap.remove(treeMap.firstKey());
                    }
                }
                dataMap.put(key, treeMap);
            } finally {
                dataMap.unlock(key);
            }

            if (0 == i % printStep) {
                log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                temp = System.currentTimeMillis();
            }
        }
        log.info("总耗时:{}", System.currentTimeMillis() - start);
        return Constants.MSG_SUCCESS;
    }
}

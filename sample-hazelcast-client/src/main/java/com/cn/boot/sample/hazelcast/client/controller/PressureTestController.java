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
     * 测试结果： 写入数量：100万，TPS：25316/s，并发线程数：32
     *
     * @param threadCount 线程数
     * @param total       数据总量
     * @param printStep   打印日志步长
     */
    @ApiOperation("1、MAP<K,Object>写入测试")
    @PostMapping("/map/object")
    public String addMapObject(int threadCount, int total, int printStep) {
        // 初始化线程池
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int count = total / threadCount;
        String data = RandomUtil.randomNumbers(75);
        String mapName = "mapObject";

        IMap<String, User> dataMap = hzInstance.getMap(mapName);

        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            String key = IdUtil.simpleUUID();
            log.info("mapName = {}, key = {}", mapName, key);
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
        };

        for (int j = 0; j < threadCount; j++) {
            // 多线程操作
            threadPool.execute(task);
        }
        return Constants.MSG_SUCCESS;
    }

    /**
     * 读取次数：100万，QPS：30840/s，并发线程数：32
     */
    @ApiOperation("2、MAP<K,Object>读取测试")
    @GetMapping("/map/object")
    public String getMapObject(String key, int threadCount, int total, int printStep) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int count = total / threadCount;
        String mapName = "mapObject";
        log.info("mapName = {}, key = {}", mapName, key);
        IMap<String, User> dataMap = hzInstance.getMap(mapName);

        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            for (int i = 0; i < count; i++) {
                dataMap.get(key);
                if (0 == i % printStep) {
                    log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                    temp = System.currentTimeMillis();
                }
            }
            log.info("总耗时:{}", System.currentTimeMillis() - start);
        };

        for (int j = 0; j < threadCount; j++) {
            threadPool.execute(task);
        }
        return Constants.MSG_SUCCESS;
    }

    /**
     * 写入数量：100万，TPS：26680/s，并发线程数：32
     */
    @ApiOperation("3、MultiMap<K,Object>写入测试")
    @PostMapping("/multimap/object")
    public String addMultimapObject(int threadCount, int total, int printStep) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int count = total / threadCount;
        String mapName = "multimapObject";
        String data = RandomUtil.randomNumbers(70);
        MultiMap<String, User> dataMap = hzInstance.getMultiMap(mapName);

        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            String key = IdUtil.simpleUUID();
            log.info("mapName = {}, key = {}", mapName, key);

            User user = new User();
            for (int i = 0; i < count; i++) {
                user.setId(key + i);
                user.setUsername(data);
                dataMap.put(key + i, user);
                if (0 == i % printStep) {
                    log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                    temp = System.currentTimeMillis();
                }
            }
            log.info("总耗时:{}", System.currentTimeMillis() - start);
        };

        for (int j = 0; j < threadCount; j++) {
            threadPool.execute(task);
        }
        return Constants.MSG_SUCCESS;
    }

    /**
     * 读取次数：100万，QPS：40241/s，并发线程数：32
     */
    @ApiOperation("4、MultiMap<K,Object>读取测试")
    @GetMapping("/multimap/object")
    public String getMultimapObject(String key, int threadCount, int total, int printStep) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int count = total / threadCount;
        String mapName = "multimapObject";
        log.info("mapName = {}, key = {}", mapName, key);
        MultiMap<String, User> dataMap = hzInstance.getMultiMap(mapName);

        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            for (int i = 0; i < count; i++) {
                dataMap.get(key);
                if (0 == i % printStep) {
                    log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                    temp = System.currentTimeMillis();
                }
            }
            log.info("总耗时:{}", System.currentTimeMillis() - start);
        };

        for (int j = 0; j < threadCount; j++) {
            threadPool.execute(task);
        }
        return Constants.MSG_SUCCESS;
    }

    /**
     * 模拟真实业务场景
     * 加锁->取出TreeMap->插入最新数据->删除最早数据，保持100条->释放锁
     * 写入次数：100万，TPS：671/s
     */
    @ApiOperation("5、MAP<K,TreeMap<K,V>>写入测试,加锁")
    @PostMapping("/map/treemap/lock")
    public String addMapTreeMap(int threadCount, int total, int printStep) throws InterruptedException {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int count = total / threadCount;
        String mapName = "mapTreeMap";
        IMap<String, TreeMap<Long, Long>> dataMap = hzInstance.getMap(mapName);

        AtomicLong totalTime = new AtomicLong();
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            String key = "g" + Thread.currentThread().getId();
            log.info("mapName = {}, key = {}", mapName, key);

            AtomicLong atomicLong = new AtomicLong(System.currentTimeMillis());
            for (int i = 0; i < count; i++) {
                dataMap.lock(key);
                try {
                    TreeMap<Long, Long> treeMap = dataMap.get(key);
                    long value = atomicLong.getAndIncrement();
                    if (null == treeMap) {
                        treeMap = new TreeMap<>();
                        treeMap.put(value, value);
                    } else {
                        treeMap.put(value, value);
                        if (100 < treeMap.size()) {
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
            long useTime = System.currentTimeMillis() - start;
            log.info("总耗时:{}", useTime);
            totalTime.addAndGet(useTime);
            countDownLatch.countDown();
        };

        for (int j = 0; j < threadCount; j++) {
            threadPool.execute(task);
        }
        countDownLatch.await();
        log.info("totalTime:{}", totalTime);
        return Constants.MSG_SUCCESS;
    }
}

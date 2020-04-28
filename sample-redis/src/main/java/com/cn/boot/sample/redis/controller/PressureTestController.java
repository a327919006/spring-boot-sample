package com.cn.boot.sample.redis.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.model.po.User;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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
    private JedisPool jedisPool;

    @Value("${spring.redis.database}")
    private int database;

    /**
     * 写入数据：100万，QPS：8849/s，并发线程数：32
     *
     * @param threadCount 线程数
     * @param total       数据总量
     * @param printStep   打印日志步长
     */
    @ApiOperation("1、set命令测试")
    @PostMapping("/set")
    public String set(int threadCount, int total, int printStep) {
        // 初始化线程池
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int count = total / threadCount;
        String data = RandomUtil.randomNumbers(75);

        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            User user = new User();
            String key = IdUtil.simpleUUID();
            log.info("key = {}", key);
            for (int i = 0; i < count; i++) {
                Jedis jedis = jedisPool.getResource();
                try {
                    user.setId(key + i);
                    user.setUsername(data);

                    jedis.set(key + i, JSONObject.toJSONString(user));
                    if (0 == i % printStep) {
                        log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                        temp = System.currentTimeMillis();
                    }
                } finally {
                    jedis.close();
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
    @ApiOperation("2、get命令测试")
    @GetMapping("/get")
    public String get(String key, int threadCount, int total, int printStep) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int count = total / threadCount;

        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            for (int i = 0; i < count; i++) {
                Jedis jedis = jedisPool.getResource();
                try {
                    jedis.get(key);
                    if (0 == i % printStep) {
                        log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                        temp = System.currentTimeMillis();
                    }
                } finally {
                    jedis.close();
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
    @ApiOperation("3、sadd命令测试")
    @PostMapping("/sadd")
    public String addMultimapObject(int threadCount, int total, int printStep) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int count = total / threadCount;
        String data = RandomUtil.randomNumbers(70);

        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            String key = IdUtil.simpleUUID();
            log.info("key = {}", key);

            User user = new User();
            for (int i = 0; i < count; i++) {
                Jedis jedis = jedisPool.getResource();
                try {
                    user.setId(key + i);
                    user.setUsername(data);
                    jedis.sadd(key + i, JSONObject.toJSONString(user));
                    if (0 == i % printStep) {
                        log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                        temp = System.currentTimeMillis();
                    }
                } finally {
                    jedis.close();
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
    @ApiOperation("4、smembers命令测试")
    @GetMapping("/smembers")
    public String smembers(String key, int threadCount, int total, int printStep) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int count = total / threadCount;

        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            for (int i = 0; i < count; i++) {
                Jedis jedis = jedisPool.getResource();
                try {
                    jedis.smembers(key);
                    if (0 == i % printStep) {
                        log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                        temp = System.currentTimeMillis();
                    }
                } finally {
                    jedis.close();
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
     * key数量：1，写入次数：100万，TPS：671/s
     * key数量：32，写入次数：100万，TPS：2594/s
     */
    @ApiOperation("5、zset保持100条数据测试")
    @PostMapping("/zset/keep")
    public String zset(int threadCount, int total, int printStep) throws InterruptedException {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int count = total / threadCount;

        AtomicLong totalTime = new AtomicLong();
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            String key = "g" + Thread.currentThread().getId();
            log.info("key = {}", key);

            AtomicLong atomicLong = new AtomicLong(System.currentTimeMillis());
            for (int i = 0; i < count; i++) {
                Jedis jedis = jedisPool.getResource();
                try {
                    long mid = atomicLong.getAndIncrement();
                    jedis.zadd(key, (double) mid, String.valueOf(mid));
                    Long msgCount = jedis.zcard(key);
                    if (100 < msgCount) {
                        long deletCount = msgCount - 100;
                        jedis.zremrangeByRank(key, 0, deletCount - 1);
                    }

                    if (0 == i % printStep) {
                        log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                        temp = System.currentTimeMillis();
                    }
                } finally {
                    jedis.close();
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

    /**
     *
     */
    @ApiOperation("6、incr命令测试")
    @GetMapping("/incr")
    public String incr(String key, int threadCount, int total, int printStep) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int count = total / threadCount;

        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            for (int i = 0; i < count; i++) {
                Jedis jedis = jedisPool.getResource();
                try {
                    jedis.incr(key);
                    if (0 == i % printStep) {
                        log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                        temp = System.currentTimeMillis();
                    }
                } finally {
                    jedis.close();
                }
            }
            log.info("总耗时:{}", System.currentTimeMillis() - start);
        };

        for (int j = 0; j < threadCount; j++) {
            threadPool.execute(task);
        }
        return Constants.MSG_SUCCESS;
    }
}

package com.cn.boot.sample.redis.controller;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/lock/test")
@Api(tags = "3、分布式锁测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class LockTestController {

    @Reference
    private RedisService redisService;
    @Autowired
    private Redisson redisson;

    private int num = 0;

    @ApiOperation("1、set命令实现(有瑕疵，不会自动延期)")
    @GetMapping("")
    public String lock(String key, int count) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("test")
                .build();
        // 确认线程池参数
        ExecutorService executor = new ThreadPoolExecutor(500, 500,
                60000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(102400), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < count; i++) {
            executor.execute(() -> {
                String value = IdUtil.simpleUUID();
                boolean lock = redisService.lock(key, value);
                if (lock) {
                    try {
                        log.info("======in==========");
                        ThreadUtil.sleep(100);
                    } finally {
                        log.info("out");
                        redisService.unlock(key, value);
                    }
                }
            });
        }

        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("2、Redisson实现（会自动延期）")
    @GetMapping("redisson")
    public String redissonLock(String key, int count, long timeout, long businessTime) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("test")
                .build();
        // 确认线程池参数
        ExecutorService executor = new ThreadPoolExecutor(500, 500,
                60000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(102400), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < count; i++) {
            executor.execute(() -> {
                RLock lock = redisson.getLock(key);

                if (0 == timeout) {
                    // 不传参数时，leaseTime默认-1，会使用看门狗，锁过期时间为30秒，每10s自动续期，也可修改Config
                    // 未获取到锁的线程阻塞等待
                    lock.lock();
                } else {
                    // 传入leaseTime指定释放锁时间，不会使用看门狗，到期自动释放锁
                    lock.lock(timeout, TimeUnit.MILLISECONDS);
                }
                try {
                    num++;
                    log.info("num={}", num);
                    ThreadUtil.sleep(businessTime);
                } finally {
                    log.info("out");
                    lock.unlock();
                }
            });
        }

        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("3、Redisson-TryLock(推荐)")
    @GetMapping("redisson/tryLock")
    public String redissonTryLock(String key, int count, long businessTime) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("test")
                .build();
        // 确认线程池参数
        ExecutorService executor = new ThreadPoolExecutor(500, 500,
                60000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(102400), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < count; i++) {
            executor.execute(() -> {
                RLock lock = redisson.getLock(key);

                boolean result = false;
                try {
                    // 获取到锁的线程返回true，未获取锁的线程返回false
                    // 支持传入waitTime，表示同步等待锁时长(默认-1不等待)，获取不到会重试获取，不会立刻返回false
                    result = lock.tryLock();
                    // result = lock.tryLock(1, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (result) {
                    try {
                        num++;
                        log.info("num={}", num);
                        ThreadUtil.sleep(businessTime);
                    } finally {
                        log.info("out");
                        lock.unlock();
                    }
                } else {
                    log.info("未获取到锁000");
                }
            });
        }

        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("4、Redisson-联锁MultiLock")
    @GetMapping("redisson/multiLock")
    public String redissonMultiLock(String key, int count, long businessTime) {
        // 生产环境，这里要用3个redis集群，本地测试用1个做测试
        // 三个集群都对key上锁成功才算联锁上锁成功
        RLock lock1 = redisson.getLock(key + "1");
        RLock lock2 = redisson.getLock(key + "2");
        RLock lock3 = redisson.getLock(key + "3");
        RLock lock = redisson.getMultiLock(lock1, lock2, lock3);

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("test")
                .build();
        // 确认线程池参数
        ExecutorService executor = new ThreadPoolExecutor(500, 500,
                60000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(102400), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < count; i++) {
            executor.execute(() -> {

                boolean result = false;
                try {
                    // 获取到锁的线程返回true，未获取锁的线程返回false
                    // 支持传入waitTime，表示同步等待锁时长(默认-1不等待)，获取不到会重试获取，不会立刻返回false
                    result = lock.tryLock();
                    // result = lock.tryLock(1, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (result) {
                    try {
                        num++;
                        log.info("num={}", num);
                        ThreadUtil.sleep(businessTime);
                    } finally {
                        log.info("out");
                        lock.unlock();
                    }
                } else {
                    log.info("未获取到锁000");
                }
            });
        }

        return Constants.MSG_SUCCESS;
    }
}

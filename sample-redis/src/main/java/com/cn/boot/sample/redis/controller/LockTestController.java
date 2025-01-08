package com.cn.boot.sample.redis.controller;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.thread.ThreadUtil;
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

    @ApiOperation("测试(有瑕疵，不会自动延期)")
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
                boolean lock = redisService.lock(key, key);
                if (lock) {
                    try {
                        log.info("======in==========");
                        ThreadUtil.sleep(100);
                    } finally {
                        log.info("out");
                        redisService.unlock(key);
                    }
                }
            });
        }

        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("测试（使用Redisson会自动延期）")
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
                    // 会使用看门狗，自动续期，默认为30秒，也可修改Config
                    // 未获取到锁的线程阻塞等待
                    lock.lock();
                } else {
                    // 不会使用看门狗，到期自动释放锁
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

    @ApiOperation("测试（Redisson-TryLock）")
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

                // 获取到锁的线程返回true，未获取锁的线程返回false
                boolean result = lock.tryLock();
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

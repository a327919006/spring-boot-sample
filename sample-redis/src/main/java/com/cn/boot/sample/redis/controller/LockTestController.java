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
@Api(tags = "分布式锁测试", produces = MediaType.APPLICATION_JSON_VALUE)
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
    public String redissonLock(String key, int count) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("test")
                .build();
        // 确认线程池参数
        ExecutorService executor = new ThreadPoolExecutor(500, 500,
                60000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(102400), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < count; i++) {
            executor.execute(() -> {
                RLock redissonLock = redisson.getLock(key);
                redissonLock.lock();
                try {
                    num++;
                    log.info("num={}", num);
                    ThreadUtil.sleep(10);
                } finally {
                    log.info("out");
                    redissonLock.unlock();
                }
            });
        }

        return Constants.MSG_SUCCESS;
    }
}

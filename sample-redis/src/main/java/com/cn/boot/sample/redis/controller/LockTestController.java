package com.cn.boot.sample.redis.controller;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.thread.ThreadUtil;
import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
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

    @ApiOperation("测试")
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
}

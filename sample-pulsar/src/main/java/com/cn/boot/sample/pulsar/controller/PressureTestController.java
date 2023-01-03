package com.cn.boot.sample.pulsar.controller;

import cn.hutool.core.util.IdUtil;
import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.pulsar.producer.TestProducer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/pressure/test")
@Api(tags = "压力测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class PressureTestController {

    @Autowired
    private TestProducer testProducer;

    /**
     * 写入数据：1000万，TPS：166655/s，并发线程数：10
     *
     * @param threadCount 线程数
     * @param total       数据总量
     * @param printStep   打印日志步长
     */
    @ApiOperation("发送")
    @PostMapping("/producer")
    public String set(int threadCount, int total, int printStep) {
        // 初始化线程池
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        int count = total / threadCount;
        String data = "1234567890123456789012345678901234567890123456789012345678901234567890";

        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            String key = IdUtil.simpleUUID();
            log.info("key = {}", key);
            for (int i = 0; i < count; i++) {
                testProducer.sendMsg(key, data);
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
}

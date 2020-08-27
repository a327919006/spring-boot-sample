package com.cn.boot.sample.logback.util;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Slf4j
public class ExecutorUtil {
    /**
     * 创建线程池
     *
     * @param name     线程池名称
     * @param coreSize 线程池大小
     * @return 线程池
     */
    public static ThreadPoolExecutor create(String name, int coreSize) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNamePrefix(name)
            .build();
        // 确认线程池参数
        return new ThreadPoolExecutor(coreSize, coreSize,
            60000L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(50000), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
}

package com.cn.boot.sample.logback.executor;

import com.cn.boot.sample.logback.util.ExecutorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 消息通知线程池
 *
 * @author Chen Nan
 */
@Slf4j
@Component
public class NotifyExecutor {
    private static final String THREAD_POOL_NAME = "notify-pool-";

    private ThreadPoolExecutor executor;

    @PostConstruct
    public void init() {
        int threadNum = Runtime.getRuntime().availableProcessors() * 8;
        executor = ExecutorUtil.create(THREAD_POOL_NAME, threadNum);
    }

    public boolean execute(Runnable thread) {
        try {
            executor.execute(thread);
            return true;
        } catch (RejectedExecutionException e) {
            // 线程池拒绝
            log.error("execute notifyMsg thread poll rejected");
            return false;
        }
    }

    public void stop() {
        executor.shutdown();
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }
}

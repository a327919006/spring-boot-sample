package com.cn.boot.sample.guava.concurrent;

import cn.hutool.core.thread.ThreadUtil;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Chen Nan
 */
@Slf4j
public class MonitorTest {

    private static LinkedList<Integer> queue = new LinkedList<>();
    private static final int MAX = 10;
    private Monitor monitor = new Monitor();
    private Monitor.Guard canOffer = monitor.newGuard(() -> queue.size() < MAX);
    private Monitor.Guard canTake = monitor.newGuard(() -> !queue.isEmpty());

    @Test
    public void test() {

        // 初始化线程池
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService threadPool = new ThreadPoolExecutor(6, 6,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        AtomicInteger count = new AtomicInteger(0);

        Runnable offerTask = () -> {
            for (; ; ) {
                int data = count.incrementAndGet();
                log.info("offer {}", data);
                offer(data);
                ThreadUtil.sleep(2);
            }
        };
        Runnable takeTask = () -> {
            for (; ; ) {
                Integer data = take();
                log.info("take {}", data);
                ThreadUtil.sleep(1);
            }
        };

        for (int j = 0; j < 3; j++) {
            // 多线程操作
            threadPool.execute(offerTask);
        }

        for (int j = 0; j < 2; j++) {
            // 多线程操作
            threadPool.execute(takeTask);
        }

        ThreadUtil.sleep(5000);
    }

    private void offer(Integer data) {
        try {
            monitor.enterWhen(canOffer);
            queue.addLast(data);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            monitor.leave();
        }
    }

    private Integer take() {
        try {
            monitor.enterWhen(canTake);
            return queue.removeFirst();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            monitor.leave();
        }
    }

}

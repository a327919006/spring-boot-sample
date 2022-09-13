package com.cn.boot.sample.tdengine.controller;

import cn.hutool.core.util.RandomUtil;
import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.tdengine.model.po.Pile;
import com.cn.boot.sample.tdengine.service.PileService;
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
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping("/benchmark")
@Api(tags = "压力测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class PressureTestController {

    @Autowired
    private PileService pileService;

    @ApiOperation("写入")
    @PostMapping("")
    public String insert(int threadCount, int total, int printStep) throws Exception {
        int count = total / threadCount;

        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger totalTps = new AtomicInteger(0);
        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            Pile pile = new Pile();
            for (int i = 1; i <= count; i++) {
                try {
                    pile.setTimestamp(System.currentTimeMillis());
                    pile.setPower(RandomUtil.randomDouble());
                    pile.setStatus(RandomUtil.randomDouble());
                    pileService.insertPile(pile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (0 == i % printStep) {
                    log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                    temp = System.currentTimeMillis();
                }
            }
            long cost = System.currentTimeMillis() - start;
            log.info("总耗时:{}", cost);
            if (cost > 0) {
                int tps = (int) (count / (cost / 1000));
                totalTps.addAndGet(tps);
                log.info("tps={}", tps);
            }
            latch.countDown();
        };

        // 启动多线程任务
        runTask(threadCount, task);

        // 等待任务结束
        latch.await();
        log.info("totalTps={}", totalTps.get());
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("批量写入")
    @PostMapping("batch")
    public String batchInsert(int threadCount, int total, int printStep) throws Exception {
        int count = total / threadCount;

        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger totalTps = new AtomicInteger(0);
        AtomicInteger number = new AtomicInteger(1000);
        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            Pile pile = new Pile();
            for (int i = 1; i <= count; i++) {
                try {
                    pile.setTimestamp(System.currentTimeMillis());
                    pile.setPower(RandomUtil.randomDouble());
                    pile.setStatus(RandomUtil.randomDouble());
                    pileService.insertPile(pile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (0 == i % printStep) {
                    log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                    temp = System.currentTimeMillis();
                }
            }
            long cost = System.currentTimeMillis() - start;
            log.info("总耗时:{}", cost);
            if (cost > 0) {
                int tps = (int) (count / (cost / 1000));
                totalTps.addAndGet(tps);
                log.info("tps={}", tps);
            }
            latch.countDown();
        };

        // 启动多线程任务
        runTask(threadCount, task);

        // 等待任务结束
        latch.await();
        log.info("totalTps={}", totalTps.get());
        return Constants.MSG_SUCCESS;
    }

    /**
     * 启动多线程任务
     */
    private void runTask(int threadCount, Runnable task) {
        ExecutorService threadPool = createThreadPool(threadCount);
        for (int j = 0; j < threadCount; j++) {
            threadPool.execute(task);
        }
    }

    /**
     * 创建线程池
     */
    private ExecutorService createThreadPool(int threadCount) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        return new ThreadPoolExecutor(threadCount, threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
}

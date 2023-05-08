package com.cn.boot.sample.es.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.es.model.dto.StudentAddReq;
import com.cn.boot.sample.es.util.ElasticsearchUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/press")
@Api(tags = "压力测试(原生API)", produces = MediaType.APPLICATION_JSON_VALUE)
public class PressController {

    private static final String INDEX = "student";

    @Autowired
    private ElasticsearchUtil elasticsearchUtil;

    @ApiOperation("测试-添加")
    @PostMapping("")
    public boolean insert(int count, int skip) {
        StudentAddReq req = new StudentAddReq();
        for (int i = skip; i < count + skip; i++) {
            req.setId("" + i);
            req.setName("name-" + i);
            req.setAge(i);
            req.setCreateTime(LocalDateTime.now());
            boolean result = elasticsearchUtil.save(INDEX, req.getId(), req);
            log.info("index={}, result={}", i, result);
        }
        return true;
    }

    @ApiOperation("测试-更新")
    @PutMapping("")
    public boolean update(int count, int skip) {
        StudentAddReq req = new StudentAddReq();
        for (int i = skip; i < count + skip; i++) {
            req.setId("" + i);
            req.setName("name-" + i);
            req.setAge(i);
            req.setCreateTime(LocalDateTime.now());
            boolean result = elasticsearchUtil.update(INDEX, req);
            log.info("index={}, result={}", i, result);
        }
        return true;
    }

    @ApiOperation("批量写入")
    @PostMapping("bulk")
    public String bulk(int threadCount, int total, int printStep) throws Exception {
        int count = total / threadCount;

        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger totalTps = new AtomicInteger(0);
        AtomicInteger number = new AtomicInteger(1000);
        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            String id = String.valueOf(IdUtil.getSnowflakeNextId());
            String data = RandomUtil.randomString(256);
            LocalDateTime now = LocalDateTime.now();
            StudentAddReq req = new StudentAddReq();
            List<StudentAddReq> list = new ArrayList<>(printStep);
            for (int i = 1; i <= count; i++) {
                try {
                    req.setId(id + i);
                    req.setName(data + i);
                    req.setAge(i);
                    req.setCreateTime(now);
                    list.add(req);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (0 == i % printStep) {
                    elasticsearchUtil.bulk(INDEX, list);
                    list.clear();
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

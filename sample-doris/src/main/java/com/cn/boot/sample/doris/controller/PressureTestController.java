package com.cn.boot.sample.doris.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.doris.model.User;
import com.cn.boot.sample.doris.service.UserService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@RequestMapping("/benchmark")
@Api(tags = "压力测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class PressureTestController {

    @Autowired
    private UserService userService;

    @ApiOperation("写入")
    @PostMapping("")
    public String insert(int threadCount, int total, int printStep) throws Exception {
        int count = total / threadCount;

        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger totalTps = new AtomicInteger(0);
        Runnable task = () -> {
            long start = System.currentTimeMillis();
            long temp = System.currentTimeMillis();

            User user = new User();
            Long username = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username1 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username2 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username3 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username4 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username5 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username6 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username7 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username8 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username9 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username10 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username11 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username12 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username13 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username14 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username15 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username16 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username17 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username18 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username19 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username20 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username21 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username22 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username23 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username24 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username25 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username26 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username27 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username28 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            Long username29 = IdUtil.getSnowflakeNextId() + RandomUtil.randomInt(0, 10000);
            List<User> list = new ArrayList<>();
            for (int i = 1; i <= count; i++) {
                try {
                    user.setUsername(username + count);
                    user.setUsername1(username1 + count);
                    user.setUsername2(username2 + count);
                    user.setUsername3(username3 + count);
                    user.setUsername4(username4 + count);
                    user.setUsername5(username5 + count);
                    user.setUsername6(username6 + count);
                    user.setUsername7(username7 + count);
                    user.setUsername8(username8 + count);
                    user.setUsername9(username9 + count);
                    user.setUsername10(username10 + count);
                    user.setUsername11(username11 + count);
                    user.setUsername12(username12 + count);
                    user.setUsername13(username13 + count);
                    user.setUsername14(username14 + count);
                    user.setUsername15(username15 + count);
                    user.setUsername16(username16 + count);
                    user.setUsername17(username17 + count);
                    user.setUsername18(username18 + count);
                    user.setUsername19(username19 + count);
                    user.setUsername20(username20 + count);
                    user.setUsername21(username21 + count);
                    user.setUsername22(username22 + count);
                    user.setUsername23(username23 + count);
                    user.setUsername24(username24 + count);
                    user.setUsername25(username25 + count);
                    user.setUsername26(username26 + count);
                    user.setUsername27(username27 + count);
                    user.setUsername28(username28 + count);
                    user.setUsername29(username29 + count);
                    list.add(user);
                    if (list.size() == 100) {
                        userService.insert(list);
                        list.clear();
                    }
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

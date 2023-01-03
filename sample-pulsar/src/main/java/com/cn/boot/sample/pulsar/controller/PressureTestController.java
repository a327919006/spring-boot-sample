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
        String data = "{\"deviceName\":\"时代星云厂站\",\"did\":\"350105000001\",\"id\":\"9af05a316d884f64ab7af0119fd3122b\",\"identifier\":\"property\",\"manufacturerId\":\"1466649541076131842\",\"manufacturerName\":\"北京索英电气技术有限公司\",\"method\":\"module.property.post\",\"mid\":\"CHARGE-5\",\"moduleId\":\"9\",\"moduleIdentify\":\"CHARGE\",\"moduleManufacturerId\":\"1546747265628569601\",\"moduleModelId\":\"1586924348355420161\",\"params\":{\"time\":1668646045945,\"value\":{\"STATUS\":{\"CaptureTime\":1668646045945,\"ChPileID\":\"5\",\"PileWarn\":\"200\",\"RunAllSta\":0,\"ChGunID\":\"1\",\"PileSta\":0}}},\"productId\":\"1\",\"productModuleIdentify\":\"CHARGE\",\"topic\":\"/alink/350105000001/CHARGE-5/module/property/post\",\"version\":\"1.1.3\"}";

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

package com.cn.boot.sample.actuator.task;

import com.cn.boot.sample.actuator.metric.TestMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Component
@Slf4j
public class TestTask {

    @Autowired
    private TestMetrics testMetrics;

    private long count1;
    private long count2;
    private long count3;

    @Scheduled(fixedDelay = 1000)
    public void increment1() {
        count1++;
        testMetrics.counter1.increment();
        log.info("increment1:{}", count1);
    }

    @Scheduled(fixedDelay = 10000)
    public void increment2() {
        count2++;
        testMetrics.counter2.increment();
        log.info("increment2:{}", count2);
    }

    @Scheduled(fixedDelay = 20000)
    public void increment3() {
        count3++;
        testMetrics.counter3.increment();
        log.info("increment3:{}", count3);
    }
}

package com.cn.boot.sample.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 使用SpringTask框架提供的定时任务
 *
 * @author Chen Nan
 */
@Slf4j
@Component
public class SampleSpringTaskJob {

    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() {
        log.info("SampleSpringTaskJob:{}", System.currentTimeMillis());
    }
}

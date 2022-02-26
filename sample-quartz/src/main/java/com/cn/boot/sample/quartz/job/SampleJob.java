package com.cn.boot.sample.quartz.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cn.boot.sample.quartz.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @author Chen Nan
 */
@Slf4j
public class SampleJob implements BaseJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String name = context.getJobDetail().getKey().getName();
        log.info("{}:{}", name, DateUtil.formatDateTime(new Date()));

        JobDataMap dataMap = context.getMergedJobDataMap();
        dataMap.forEach((key, value) -> {
            log.info("key={} value={}", key, value);
        });
        BusinessService bean = SpringUtil.getBean(BusinessService.class);
        bean.doJob("one");
    }
}

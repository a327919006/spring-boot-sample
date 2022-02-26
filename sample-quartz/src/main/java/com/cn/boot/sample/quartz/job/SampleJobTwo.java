package com.cn.boot.sample.quartz.job;

import cn.hutool.core.date.DateUtil;
import com.cn.boot.sample.quartz.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author Chen Nan
 */
@Slf4j
public class SampleJobTwo implements BaseJob {

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String name = context.getJobDetail().getKey().getName();
        log.info("{}:{}", name, DateUtil.formatDateTime(new Date()));

//        BusinessService businessService = SpringUtil.getBean(BusinessService.class);
        businessService.doJob("two");
    }
}

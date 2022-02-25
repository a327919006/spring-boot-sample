package com.cn.boot.sample.quartz.job;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.cn.boot.sample.quartz.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @author Chen Nan
 */
@Slf4j
public class SampleJobTwo implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("SampleJobTwo:" + DateUtil.formatDateTime(new Date()));

        BusinessService bean = SpringUtil.getBean(BusinessService.class);
        bean.doJob("two");
    }
}
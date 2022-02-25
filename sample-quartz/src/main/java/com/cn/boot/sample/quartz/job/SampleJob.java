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
        log.info("SampleJob:" + DateUtil.formatDateTime(new Date()));

        JobDataMap dataMap = context.getMergedJobDataMap();
        String param1 = dataMap.getString("param1");
        int param2 = dataMap.getInt("param2");
        log.info("param1={} param2={}", param1, param2);
        BusinessService bean = SpringUtil.getBean(BusinessService.class);
        bean.doJob("one");
    }
}

package com.cn.boot.sample.quartz.config;

import com.cn.boot.sample.quartz.job.SampleJob;
import org.quartz.JobDataMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.util.Objects;

/**
 * @author Chen Nan
 */
@Configuration
public class SpringQuartzConfig {

    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("param1", "value1");
        jobDataMap.put("param2", 22);

        JobDetailFactoryBean jobFactory = new JobDetailFactoryBean();
        jobFactory.setName("sample_job");
        jobFactory.setGroup("sample_group");
        jobFactory.setJobClass(SampleJob.class);
        jobFactory.setDurability(true);
        jobFactory.setJobDataMap(jobDataMap);
        return jobFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean simpleTriggerFactoryBean() {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setName("sample_trigger");
        trigger.setGroup("sample_group");
        trigger.setRepeatCount(3);
        trigger.setStartDelay(1000);
        trigger.setRepeatInterval(5000);
        trigger.setJobDetail(Objects.requireNonNull(jobDetailFactoryBean().getObject()));

        return trigger;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setTriggers(simpleTriggerFactoryBean().getObject());
        return scheduler;
    }
}

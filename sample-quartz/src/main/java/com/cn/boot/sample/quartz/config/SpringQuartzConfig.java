package com.cn.boot.sample.quartz.config;

import com.cn.boot.sample.quartz.job.SampleJob;
import com.cn.boot.sample.quartz.job.SampleJobTwo;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Chen Nan
 */
@Configuration
public class SpringQuartzConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JobDetailFactoryBean myJob1() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("param1", "value1");
        jobDataMap.put("param2", 22);

        JobDetailFactoryBean jobFactory = new JobDetailFactoryBean();
        jobFactory.setName("sample_job1");
        jobFactory.setGroup("sample_group");
        jobFactory.setJobClass(SampleJob.class);
        jobFactory.setDurability(true);
        jobFactory.setJobDataMap(jobDataMap);
        return jobFactory;
    }

    @Bean
    public JobDetailFactoryBean myJob2() {
        JobDetailFactoryBean jobFactory = new JobDetailFactoryBean();
        jobFactory.setName("sample_job2");
        jobFactory.setGroup("sample_group");
        jobFactory.setJobClass(SampleJobTwo.class);
        jobFactory.setDurability(true);
        return jobFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean myTrigger1(@Qualifier("myJob1") JobDetailFactoryBean myJob1) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setName("sample_trigger1");
        trigger.setGroup("sample_group");
        trigger.setRepeatCount(3);
        trigger.setStartDelay(1000);
        trigger.setRepeatInterval(5000);
        trigger.setJobDetail(Objects.requireNonNull(myJob1.getObject()));

        return trigger;
    }

    @Bean
    public CronTriggerFactoryBean myTrigger2(@Qualifier("myJob2") JobDetailFactoryBean myJob2) {
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setName("sample_trigger2");
        trigger.setGroup("sample_group");
        trigger.setCronExpression("0/2 * * * * ? ");
        trigger.setJobDetail(Objects.requireNonNull(myJob2.getObject()));

        return trigger;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(SimpleTriggerFactoryBean myTrigger1,
                                                     CronTriggerFactoryBean myTrigger2) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("quartz.properties");

        InputStream inputStream = classPathResource.getInputStream();
        Properties properties = new Properties();
        properties.load(inputStream);

        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setDataSource(dataSource);
        scheduler.setQuartzProperties(properties);
        scheduler.setTriggers(myTrigger1.getObject(), myTrigger2.getObject());
        return scheduler;
    }
}

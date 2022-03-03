package com.cn.boot.sample.quartz.config;

import com.cn.boot.sample.quartz.job.SampleJob;
import com.cn.boot.sample.quartz.job.SampleJobTwo;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * 使用Spring封装的QuartzApi声明定时任务
 *
 * @author Chen Nan
 */
@Configuration
public class SpringQuartzConfig {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private CustomJobFactory customJobFactory;

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
        trigger.setCronExpression("0/10 * * * * ? ");
        trigger.setJobDetail(Objects.requireNonNull(myJob2.getObject()));

        // 设置错过的策略，错过(1-N次)后只执行一次
        // MISFIRE_INSTRUCTION_DO_NOTHING错过后不执行
        // 默认：错过后都执行
        trigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
        return trigger;
    }

    @Bean
    public SchedulerFactoryBean scheduler(SimpleTriggerFactoryBean myTrigger1,
                                          CronTriggerFactoryBean myTrigger2) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("quartz.properties");

        InputStream inputStream = classPathResource.getInputStream();
        Properties properties = new Properties();
        properties.load(inputStream);

        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setDataSource(dataSource);
        // 可选，QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        scheduler.setOverwriteExistingJobs(true);
        // 必须的，QuartzScheduler 延时启动，应用启动完后 QuartzScheduler 再启动
        scheduler.setStartupDelay(3);
        // 让任务在程序启动时加载
        scheduler.setAutoStartup(true);
        scheduler.setJobFactory(customJobFactory);
        scheduler.setApplicationContextSchedulerContextKey("applicationContextKey");

        scheduler.setQuartzProperties(properties);
        scheduler.setTriggers(myTrigger1.getObject(), myTrigger2.getObject());
        return scheduler;
    }
}

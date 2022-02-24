package com.cn.boot.sample.quartz.config;

import com.cn.boot.sample.quartz.job.SampleJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Chen Nan
 */
@Configuration
public class QuartzConfig {

    @PostConstruct
    public void inid() throws SchedulerException {
        // 定义job信息
        JobDetail jobDetail = JobBuilder.newJob(SampleJob.class)
                .withIdentity("job1", "group1")
                // 定义启动job时传入的参数
                .usingJobData("param1", "value1")
                .usingJobData("param2", 222)
                .build();

        // 定义触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                // 定义触发间隔
                // SimpleSchedule：支持每隔多久执行一次
                // CronScheduler：支持按cron表达式执行
                // DailyTimeIntervalScheduleBuilder：
                // CalendarIntervalScheduleBuilder：
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever())
                .build();

        SchedulerFactory factory = new StdSchedulerFactory();

        // 调度器
        Scheduler scheduler = factory.getScheduler();

        // 绑定任务与触发器，支持1个job绑定多个触发器
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }
}

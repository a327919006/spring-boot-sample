package com.cn.boot.sample.quartz.config;

import com.cn.boot.sample.quartz.job.SampleJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Chen Nan
 */
//@Configuration
public class QuartzConfig {

    @PostConstruct
    public void inid() throws SchedulerException, IOException {
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
//                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever())
                .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
                .build();

        // 读取配置文件信息
        ClassPathResource classPathResource = new ClassPathResource("quartz.properties");
        InputStream inputStream = classPathResource.getInputStream();
        Properties properties = new Properties();
        properties.load(inputStream);
        // 使用内存存储job信息
        properties.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        properties.remove("org.quartz.jobStore.driverDelegateClass");
        properties.remove("org.quartz.jobStore.tablePrefix");

        SchedulerFactory factory = new StdSchedulerFactory(properties);

        // 调度器
        Scheduler scheduler = factory.getScheduler();

        // 绑定任务与触发器，支持1个job绑定多个触发器
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }
}

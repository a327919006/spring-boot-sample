package com.cn.boot.sample.quartz.service.impl;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.model.dto.RspBase;
import com.cn.boot.sample.api.model.po.CronJob;
import com.cn.boot.sample.dal.mapper.CronJobMapper;
import com.cn.boot.sample.quartz.job.BaseJob;
import com.cn.boot.sample.quartz.service.QuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
@Service
public class QuartzJobServiceImpl extends BaseServiceImpl<CronJobMapper, CronJob, String>
        implements QuartzJobService {

    @Autowired
    private Scheduler scheduler;

    @Override
    public RspBase<String> addJob(String jobClassName, String jobGroupName, String cronExpression) {
        try {
            // 启动调度器
            scheduler.start();

            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).build();

            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            log.error("创建定时任务失败:", e);
            return RspBase.fail(Constants.MSG_FAIL);
        }
        return RspBase.success();
    }

    @Override
    public RspBase<String> jobReschedule(String jobClassName, String jobGroupName, String cronExpression) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            log.error("更新定时任务失败:", e);
            return RspBase.fail(Constants.MSG_FAIL);
        }
        return RspBase.success();
    }

    @Override
    public RspBase<String> jobDelete(String jobClassName, String jobGroupName) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (Exception e) {
            log.error("删除定时任务失败:", e);
            return RspBase.fail(Constants.MSG_FAIL);
        }
        return RspBase.success();
    }

    @Override
    public RspBase<String> jobPause(String jobClassName, String jobGroupName) {
        try {
            scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (Exception e) {
            log.error("暂停定时任务失败:", e);
            return RspBase.fail(Constants.MSG_FAIL);
        }
        return RspBase.success();
    }

    @Override
    public RspBase<String> jobResume(String jobClassName, String jobGroupName) {
        try {
            scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (Exception e) {
            log.error("恢复定时任务失败:", e);
            return RspBase.fail(Constants.MSG_FAIL);
        }
        return RspBase.success();
    }

    @Override
    public boolean checkGroupAndClassNameExist(CronJob vo) {
        CronJob cronJob = mapper.selectOne(vo);
        return cronJob != null;
    }

    public static BaseJob getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob) class1.newInstance();
    }
}

package com.cn.boot.sample.quartz.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public RspBase<String> addJob(CronJob dto) {
        try {
            boolean exist = checkGroupAndClassNameExist(dto);
            if (exist) {
                return RspBase.fail("该任务已存在");
            }
            // 启动调度器
            scheduler.start();

            String jobClassName = dto.getJobClassName();
            String jobGroup = dto.getJobGroup();
            String cron = dto.getCron();
            String jobDataMap = dto.getJobDataMap();
            JobDataMap dataMap = new JobDataMap();
            ;
            if (JSONUtil.isJson(jobDataMap)) {
                Map map = JSONUtil.toBean(jobDataMap, Map.class);
                dataMap.putAll(map);
            }
            Class<? extends BaseJob> jobClass = getClass(jobClassName).getClass();
            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobClassName, jobGroup)
                    .usingJobData(dataMap)
                    .build();

            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName,jobGroup).withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);

            String id = IdUtil.nanoId();
            dto.setId(id);
            mapper.insertSelective(dto);
            return RspBase.data(id);
        } catch (Exception e) {
            log.error("创建定时任务失败:", e);
            return RspBase.fail(Constants.MSG_FAIL);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RspBase<String> jobReschedule(CronJob dto) {
        CronJob cronJob = mapper.selectByPrimaryKey(dto.getId());
        if (cronJob == null) {
            return RspBase.fail("该任务不存在");
        }
        try {
            String jobClassName = cronJob.getJobClassName();
            String jobGroup = cronJob.getJobGroup();
            String cron = dto.getCron();

            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroup);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);

            CronJob bean = new CronJob();
            bean.setId(dto.getId());
            bean.setCron(dto.getCron());
            mapper.updateByPrimaryKeySelective(bean);
            return RspBase.success();
        } catch (SchedulerException e) {
            log.error("更新定时任务失败:", e);
            return RspBase.fail(Constants.MSG_FAIL);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RspBase<String> jobDelete(String id) {
        CronJob cronJob = mapper.selectByPrimaryKey(id);
        if (cronJob == null) {
            return RspBase.fail("该任务不存在");
        }
        try {
            String jobClassName = cronJob.getJobClassName();
            String jobGroup = cronJob.getJobGroup();
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroup));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroup));
            scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroup));

            mapper.deleteByPrimaryKey(id);
            return RspBase.success();
        } catch (Exception e) {
            log.error("删除定时任务失败:", e);
            return RspBase.fail(Constants.MSG_FAIL);
        }
    }

    @Override
    public RspBase<String> jobPause(String id) {
        CronJob cronJob = mapper.selectByPrimaryKey(id);
        if (cronJob == null) {
            return RspBase.fail("该任务不存在");
        }
        try {
            String jobClassName = cronJob.getJobClassName();
            String jobGroup = cronJob.getJobGroup();
            scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroup));

            CronJob bean = new CronJob();
            bean.setId(id);
            bean.setStatus(0);
            mapper.updateByPrimaryKeySelective(bean);
            return RspBase.success();
        } catch (Exception e) {
            log.error("暂停定时任务失败:", e);
            return RspBase.fail(Constants.MSG_FAIL);
        }
    }

    @Override
    public RspBase<String> jobResume(String id) {
        CronJob cronJob = mapper.selectByPrimaryKey(id);
        if (cronJob == null) {
            return RspBase.fail("该任务不存在");
        }
        try {
            String jobClassName = cronJob.getJobClassName();
            String jobGroup = cronJob.getJobGroup();
            scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroup));

            CronJob bean = new CronJob();
            bean.setId(id);
            bean.setStatus(1);
            mapper.updateByPrimaryKeySelective(bean);
            return RspBase.success();
        } catch (Exception e) {
            log.error("恢复定时任务失败:", e);
            return RspBase.fail(Constants.MSG_FAIL);
        }
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

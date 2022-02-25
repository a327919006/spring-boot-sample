package com.cn.boot.sample.quartz.service;

import com.cn.boot.sample.api.model.dto.RspBase;
import com.cn.boot.sample.api.model.po.CronJob;
import com.cn.boot.sample.api.service.BaseService;

/**
 * @author Chen Nan
 */
public interface QuartzJobService extends BaseService<CronJob, String> {
    /**
     * 添加任务
     */
    RspBase<String> addJob(String jobClassName, String jobGroupName, String cronExpression) throws Exception;

    /**
     * 更新任务cron表达式
     */
    RspBase<String> jobReschedule(String jobClassName, String jobGroupName, String cronExpression) throws Exception;

    /**
     * 删除任务--删除操作前应该暂停该任务的触发器，并且停止该任务的执行
     */
    RspBase<String> jobDelete(String jobClassName, String jobGroupName) throws Exception;

    /**
     * 暂停任务
     */
    RspBase<String> jobPause(String jobClassName, String jobGroupName) throws Exception;

    /**
     * 恢复任务
     */
    RspBase<String> jobResume(String jobClassName, String jobGroupName) throws Exception;

    /**
     * 校验输入的jobGroup和jobClassName是否已经存在定时任务了
     */
    boolean checkGroupAndClassNameExist(CronJob vo);
}

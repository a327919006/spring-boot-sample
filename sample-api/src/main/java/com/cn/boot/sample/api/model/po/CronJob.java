package com.cn.boot.sample.api.model.po;

import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "quartz_test.qrtz_cron_job")
public class CronJob {
    /**
     * 编号
     */
    @Id
    private String id;

    /**
     * 任务名
     */
    @Column(name = "job_name")
    private String jobName;

    /**
     * 任务组名称
     */
    @Column(name = "job_group")
    private String jobGroup;

    /**
     * job描述
     */
    @Column(name = "job_desc")
    private String jobDesc;

    /**
     * job类名
     */
    @Column(name = "job_class_name")
    private String jobClassName;

    /**
     * job参数
     */
    @Column(name = "job_data_map")
    private String jobDataMap;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 状态
     */
    private Integer status;
}
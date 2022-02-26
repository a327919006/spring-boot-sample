package com.cn.boot.sample.quartz.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import com.cn.boot.sample.api.model.po.CronJob;
import com.cn.boot.sample.quartz.service.QuartzJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/job")
@Api(tags = "quartz动态任务调度", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuartzJobController {

    @Autowired
    private QuartzJobService quartzJobService;

    @ApiOperation("添加任务")
    @PostMapping(value = "")
    public RspBase<String> add(CronJob dto) {
        return quartzJobService.addJob(dto);
    }

    @ApiOperation("修改任务-cron表达式")
    @PutMapping(value = "")
    public RspBase<String> update(CronJob dto) {
        return quartzJobService.jobReschedule(dto);
    }

    @ApiOperation("删除任务")
    @DeleteMapping("/{id}")
    public RspBase<String> delete(@PathVariable String id) {
        return quartzJobService.jobDelete(id);
    }

    @ApiOperation("暂停任务")
    @PutMapping(value = "/{id}/pause")
    public RspBase<String> pause(@PathVariable String id) {
        return quartzJobService.jobPause(id);
    }

    @ApiOperation("恢复任务")
    @PutMapping(value = "/{id}/resume")
    public RspBase<String> resume(@PathVariable String id) {
        return quartzJobService.jobResume(id);
    }

}

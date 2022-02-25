package com.cn.boot.sample.quartz.controller;

import com.cn.boot.sample.api.model.dto.RspBase;
import com.cn.boot.sample.api.model.po.CronJob;
import com.cn.boot.sample.quartz.service.QuartzJobService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/job")
public class QuartzJobController {

    @Autowired
    private QuartzJobService quartzJobService;

    /*
     * @Author WanCC
     * @Date 2021/2/2 10:05
     * @Description  添加任务
     */
    @ApiOperation("添加任务")
    @PostMapping(value = "/add")
    public RspBase add(CronJob vo) {
        // 校验输入的jobGroup和jobClassName是否已经存在定时任务了   [WanCC 2021/2/2  16:26]
        boolean exist = quartzJobService.checkGroupAndClassNameExist(vo);
        if (exist) {
            return RspBase.fail("该任务已存在");
        }
        try {
            if (StringUtils.isEmpty(vo.getId())) {
                // 插入信息
                return quartzJobService.addJob(vo.getJobClassName(), vo.getJobGroup(), vo.getCron());
            } else {
                // 修改信息
                RspBase rspBase = quartzJobService.jobReschedule(vo.getJobClassName(), vo.getJobGroup(), vo.getCron());
                if (rspBase.getCode() == PubConfigValue.RETURN_CODE_SUCCESS) {
                    result = quartzJobService.updateInfo(vo);
                    result.setData(vo.getId());
                } else {
                    result.setErrorCode(rspBase.getCode());
                    result.setMessage(rspBase.getMessage());
                }
            }
        } catch (Exception e) {
            result.setErrorCode(PubConfigValue.RETURN_CODE_ERROR);
            result.setMessage("系统出现错误，请联系管理员");
            logger.error("Failed to invoke addjob method", e);
        } finally {
            return result;
        }
    }

    @ApiOperation("删除任务")
    @DeleteMapping()
    public RspBase delete(HttpServletRequest request) throws Exception {
        RspBase rspBase = new RspBase();
        String id = request.getParameter("id");
        CronJob vo = quartzJobService.selectByPrimaryKey(id);
        try {
            rspBase = quartzJobService.jobDelete(vo.getJobClassName(), vo.getJobGroup());
            if (rspBase.getCode() == PubConfigValue.RETURN_CODE_SUCCESS) {
                vo.setStatus(PubConfigValue.JOB_STATUS_DELETE);
                rspBase = quartzJobService.updateJobStatus(vo);
            } else {
                rspBase.setErrorCode(rspBase.getCode());
                rspBase.setMessage(rspBase.getMessage());
            }
        } catch (Exception e) {
            rspBase.setErrorCode(PubConfigValue.RETURN_CODE_ERROR);
            rspBase.setMessage("系统出现错误，请联系管理员");
            logger.error("Failed to invoke deletejob method", e);
        } finally {
            return rspBase;
        }
    }

    @ApiOperation("暂停任务")
    @PostMapping(value = "/pause")
    public RspBase pause(HttpServletRequest request) throws Exception {
        RspBase rspBase = new RspBase();
        String id = request.getParameter("id");
        CronJob vo = quartzJobService.selectByPrimaryKey(id);
        try {
            rspBase = quartzJobService.jobPause(vo.getJobClassName(), vo.getJobGroup());
            if (rspBase.getCode() == PubConfigValue.RETURN_CODE_SUCCESS) {
                vo.setStatus(PubConfigValue.JOB_STATUS_PAUSE);
                rspBase = quartzJobService.updateJobStatus(vo);
            } else {
                rspBase.setErrorCode(rspBase.getCode());
                rspBase.setMessage(rspBase.getMessage());
            }
        } catch (Exception e) {
            rspBase.setErrorCode(PubConfigValue.RETURN_CODE_ERROR);
            rspBase.setMessage("系统出现错误，请联系管理员");
            logger.error("Failed to invoke pausejob method", e);
        } finally {
            return rspBase;
        }
    }

    @ApiOperation("恢复任务")
    @RequestMapping(value = "/resume")
    public RspBase resume(String id) {
        CronJob vo = quartzJobService.selectByPrimaryKey(id);
        try {
            rspBase = quartzJobService.jobResume(vo.getJobClassName(), vo.getJobGroup());
            if (rspBase.getCode() == PubConfigValue.RETURN_CODE_SUCCESS) {
                vo.setStatus(PubConfigValue.JOB_STATUS_RUN);
                rspBase = quartzJobService.updateJobStatus(vo);
            } else {
                rspBase.setErrorCode(rspBase.getCode());
                rspBase.setMessage(rspBase.getMessage());
            }
        } catch (Exception e) {
            rspBase.setErrorCode(PubConfigValue.RETURN_CODE_ERROR);
            rspBase.setMessage("系统出现错误，请联系管理员");
            logger.error("Failed to invoke resumejob method", e);
        } finally {
            return rspBase;
        }
    }

}

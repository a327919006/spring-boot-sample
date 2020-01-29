package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.exceptions.BusinessException;
import com.cn.boot.sample.api.model.dto.teacher.TeacherAddReq;
import com.cn.boot.sample.api.model.po.Teacher;
import com.cn.boot.sample.api.service.TeacherService;
import com.cn.boot.sample.api.service.UidGeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/teacher")
@Api(tags = "教师管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeacherController {

    @Reference
    private TeacherService teacherService;
    @Reference
    private UidGeneratorService uidGeneratorService;

    @ApiOperation("教师-添加")
    @PostMapping
    public Teacher insert(@RequestBody @Valid TeacherAddReq req) {

        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(req, teacher);
        teacher.setId(uidGeneratorService.generate());
        teacherService.insertSelective(teacher);
        log.info("teacherId={}", teacher.getId());
        return teacher;
    }

    @ApiOperation("教师-获取")
    @GetMapping("/{id}")
    public Teacher get(@PathVariable String id) {
        Teacher teacher = teacherService.selectByPrimaryKey(id);
        if (teacher == null) {
            throw new BusinessException("教师不存在");
        }
        return teacher;
    }
}

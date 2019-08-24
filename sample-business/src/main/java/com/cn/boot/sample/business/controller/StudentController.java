package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.dto.student.StudentAddReq;
import com.cn.boot.sample.api.model.dto.student.StudentGetReq;
import com.cn.boot.sample.api.model.po.Student;
import com.cn.boot.sample.api.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/student")
@Api(tags = "学生管理", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StudentController {

    @Reference
    private StudentService studentService;

    @ApiOperation("学生-添加")
    @PostMapping("")
    public Student insert(@RequestBody @Valid StudentAddReq req) {
        return studentService.insert(req);
    }

    @ApiOperation("学生-查询")
    @GetMapping("")
    public Student find(@ModelAttribute @Valid StudentGetReq req) {
        return studentService.findFirstByNameAndAge(req.getName(), req.getAge());
    }
}

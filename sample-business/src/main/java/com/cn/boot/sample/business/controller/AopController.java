package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.dto.ReqDTO;
import com.cn.boot.sample.api.model.po.Student;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/aop")
@Api(tags = "测试01-切面组件", produces = MediaType.APPLICATION_JSON_VALUE)
public class AopController {

    @ApiOperation("测试-参数处理器-ArgumentResolver")
    @PostMapping("argumentResolver")
    public Student argumentResolver(ReqDTO<Student> req) {
        Student data = req.getData();
        data.setName("123456");
        return data;
    }

}

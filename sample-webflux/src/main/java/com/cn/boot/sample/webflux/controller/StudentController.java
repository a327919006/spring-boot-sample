package com.cn.boot.sample.webflux.controller;

import com.cn.boot.sample.webflux.model.Student;
import com.cn.boot.sample.webflux.repository.StudentRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/student")
@Api(tags = "学生管理", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StudentController {

    @Autowired
    private StudentRepository userRepository;

    @ApiOperation("添加")
    @PostMapping
    public Mono<Student> save(@RequestBody Student student) {
        return userRepository.save(student);
    }

    @ApiOperation("列表")
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Student> list() {
        return userRepository.findAll();
    }
}

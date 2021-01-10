package com.cn.boot.sample.es.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.es.model.dto.StudentAddReq;
import com.cn.boot.sample.es.model.po.Student;
import com.cn.boot.sample.es.util.ElasticsearchUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/student")
@Api(tags = "学生管理(原生API)", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentController {

    private static final String INDEX = "student";

    @Autowired
    private ElasticsearchUtil elasticsearchUtil;

    @ApiOperation("创建索引")
    @PostMapping("/index")
    public boolean createIndex() {
        return elasticsearchUtil.createIndex(INDEX);
    }

    @ApiOperation("删除索引")
    @DeleteMapping("/index")
    public boolean delete() {
        return elasticsearchUtil.deleteIndex(INDEX);
    }

    @ApiOperation("添加")
    @PostMapping("")
    public String insert(@RequestBody StudentAddReq req) {
        req.setCreateTime(LocalDateTime.now());
        elasticsearchUtil.save(INDEX, req);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("根据ID获取")
    @GetMapping("/{id}")
    public Student getById(@PathVariable String id) {
        return elasticsearchUtil.getById(INDEX, id);
    }

    @ApiOperation("获取列表")
    @GetMapping("/all")
    public List<Student> find(int page, int size) {
        return elasticsearchUtil.findAll(INDEX, page, size);
    }

    @ApiOperation("根据name获取")
    @GetMapping("/find")
    public List<Student> find(String name) {
        return elasticsearchUtil.findByName(INDEX, name);
    }


}
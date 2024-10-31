package com.cn.boot.sample.es.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.es.model.dto.TeacherReq;
import com.cn.boot.sample.es.model.po.Teacher;
import com.cn.boot.sample.es.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/teacher")
@Api(tags = "教师管理(EasyES)", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("创建索引")
    @PostMapping("/index")
    public String createIndex() {
        if (teacherService.createIndex()) {
            return Constants.MSG_SUCCESS;
        }
        return Constants.MSG_FAIL;
    }

    @ApiOperation("删除索引")
    @DeleteMapping("/index")
    public String delete() {
        if (teacherService.deleteIndex()) {
            return Constants.MSG_SUCCESS;
        }
        return Constants.MSG_FAIL;
    }

    @ApiOperation("添加")
    @PostMapping("")
    public Integer insert(@RequestBody Teacher req) {
        return teacherService.insert(req);
    }

    @ApiOperation("更新")
    @PutMapping("")
    public Integer update(@RequestBody Teacher req) {
        return teacherService.update(req);
    }

    @ApiOperation("根据ID删除")
    @DeleteMapping("/{id}")
    public Integer deleteById(@PathVariable String id) {
        return teacherService.deleteById(id);
    }

    @ApiOperation("根据ID获取")
    @GetMapping("/{id}")
    public Teacher getById(@PathVariable String id) {
        return teacherService.getById(id);
    }

    @ApiOperation("获取列表")
    @GetMapping("/list")
    public List<Teacher> list(TeacherReq req) {
        return teacherService.list(req);
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public EsPageInfo<Teacher> page(TeacherReq req) {
        return teacherService.page(req);
    }

    @ApiOperation("获取数量")
    @GetMapping("/count")
    public long count(TeacherReq req) {
        return teacherService.count(req);
    }
}

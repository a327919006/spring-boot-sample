package com.cn.boot.sample.es.controller;

import com.cn.boot.sample.api.model.Constants;
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
        return teacherService.save(req);
    }

    @ApiOperation("更新")
    @PutMapping("")
    public Integer update(@RequestBody Teacher req) {
        return teacherService.update(req);
    }

    @ApiOperation("根据ID获取")
    @GetMapping("/{id}")
    public Teacher getById(@PathVariable String id) {
        return teacherService.getById(id);
    }

    @ApiOperation("根据ID删除")
    @DeleteMapping("/{id}")
    public Integer deleteById(@PathVariable String id) {
        return teacherService.delete(id);
    }


    @ApiOperation("获取列表")
    @GetMapping("/all")
    public List<Teacher> find() {
        return teacherService.findAll();
    }

    @ApiOperation("根据姓名获取")
    @GetMapping("/name")
    public EsPageInfo<Teacher> findByContent(@RequestParam String name, @RequestParam int page,
                                             @RequestParam int size) {
        return teacherService.findByName(name, page, size);
    }

    @ApiOperation("根据name获取数量")
    @GetMapping("/count")
    public long count(@RequestParam String name) {
        return teacherService.countByName(name);
    }
}

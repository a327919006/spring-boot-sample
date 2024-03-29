package com.cn.boot.sample.es.controller;

import com.cn.boot.sample.es.model.dto.StudentAddReq;
import com.cn.boot.sample.es.model.dto.StudentGetReq;
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
import java.util.Map;

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
    public boolean insert(@RequestBody StudentAddReq req) {
        req.setCreateTime(LocalDateTime.now());
        return elasticsearchUtil.save(INDEX, req.getId(), req);
    }

    @ApiOperation("批量添加")
    @PostMapping("/bulk")
    public boolean insert(@RequestBody List<StudentAddReq> list) {
        return elasticsearchUtil.bulk(INDEX, list);
    }

    @ApiOperation("更新")
    @PutMapping("")
    public boolean update(@RequestBody StudentAddReq req) {
        return elasticsearchUtil.update(INDEX, req);
    }

    @ApiOperation("根据ID获取")
    @GetMapping("/{id}")
    public Student getById(@PathVariable String id) {
        return elasticsearchUtil.getById(INDEX, id);
    }

    @ApiOperation("根据ID删除")
    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable String id) {
        return elasticsearchUtil.delete(INDEX, id);
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

    @ApiOperation("根据name获取数量")
    @GetMapping("/count")
    public long count(String name) {
        return elasticsearchUtil.countByName(INDEX, name);
    }

    @ApiOperation("根据name和age获取")
    @GetMapping("/findByNameAndAge")
    public List<Student> findByNameAndAge(StudentGetReq req) {
        return elasticsearchUtil.findByNameAndAge(INDEX, req.getNameList(), req.getAge());
    }

    @ApiOperation("scroll")
    @GetMapping("/scroll")
    public List<Student> scroll(String name) {
        return elasticsearchUtil.scroll(INDEX, name);
    }

    @ApiOperation("获取平均年龄")
    @GetMapping("/age/avg")
    public Double getAvgAge(String name) {
        return elasticsearchUtil.getAvgAge(INDEX, name);
    }

    @ApiOperation("获取年龄范围")
    @GetMapping("/age/min")
    public Map<String, String> getMinAgeGroupByName() {
        return elasticsearchUtil.getAgeGroupByName(INDEX);
    }
}

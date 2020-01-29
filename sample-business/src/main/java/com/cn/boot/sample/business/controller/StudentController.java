package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.dto.DataGrid;
import com.cn.boot.sample.api.model.dto.student.StudentAddReq;
import com.cn.boot.sample.api.model.dto.student.StudentGetReq;
import com.cn.boot.sample.api.model.po.Student;
import com.cn.boot.sample.api.model.vo.student.StudentRsp;
import com.cn.boot.sample.api.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/student")
@Api(tags = "学生管理", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation("学生-DataGrid")
    @GetMapping("/list/page")
    public DataGrid listPage(@ModelAttribute @Valid StudentGetReq req) {
        return studentService.listPage(req.getName(), req.getAge());
    }

    @ApiOperation("学生-分页")
    @GetMapping("/list")
    public List<Student> list(@ModelAttribute @Valid StudentGetReq req) {
        return studentService.list(req.getName(), req.getAge());
    }

    @ApiOperation("学生ID、姓名列表")
    @GetMapping("/list/info")
    public List<Student> listIdAndName(@ModelAttribute @Valid StudentGetReq req) {
        return studentService.findIdAndName(req.getName(), req.getAge());
    }

    @ApiOperation("学生ID、姓名列表分页")
    @GetMapping("/list/info/page")
    public DataGrid listIdAndNamePage(@ModelAttribute @Valid StudentGetReq req) {
        return studentService.findIdAndNamePage(req.getName(), req.getAge());
    }

    @ApiOperation("根据姓名获取列表")
    @GetMapping("/list/name")
    public List<Student> listByName(@RequestParam String name) {
        return studentService.findByName(name);
    }

    @ApiOperation("根据姓名更新年龄")
    @PutMapping("/{name}/age")
    public int updateAgeByName(@RequestParam Integer age, @PathVariable String name) {
        return studentService.updateAgeByName(age, name);
    }

    @ApiOperation("插入/更新,使用on duplicate key update")
    @PostMapping("/upsert")
    public int upsert(@RequestBody Student student) {
        return studentService.upsert(student);
    }

    @ApiOperation("插入信息，使用自定义对象StudentAddReq")
    @PostMapping("/insert/info")
    public int insertInfo(@RequestBody @Valid StudentAddReq req) {
        return studentService.insertInfo(req);
    }

    @ApiOperation("查询，使用IN List")
    @GetMapping("/findByIdList")
    public List<Student> findByIdList(@RequestParam List<String> idList, @RequestParam int age) {
        return studentService.findByIdList(idList, age);
    }

    @ApiOperation("查询，返回自定义对象")
    @GetMapping("/findNameByAge")
    public List<StudentRsp> findNameByAge(@RequestParam int age) {
        return studentService.findNameByAge(age);
    }
}

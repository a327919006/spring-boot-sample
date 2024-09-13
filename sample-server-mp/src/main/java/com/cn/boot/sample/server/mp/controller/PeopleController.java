package com.cn.boot.sample.server.mp.controller;

import com.cn.boot.sample.dal.mp.model.dto.PeopleDTO;
import com.cn.boot.sample.dal.mp.model.po.People;
import com.cn.boot.sample.server.mp.service.IPeopleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/people")
@Api(tags = "", produces = MediaType.APPLICATION_JSON_VALUE)
public class PeopleController {

    @Autowired
    private IPeopleService peopleService;

    @ApiOperation("save")
    @PostMapping("")
    public Long save(@RequestBody PeopleDTO dto) {
        People people = new People();
        BeanUtils.copyProperties(dto, people);
        peopleService.save(people);

        return people.getId();
    }

    @ApiOperation("delete")
    @DeleteMapping("{id}")
    public boolean delete(@PathVariable String id) {
        return peopleService.removeById(id);
    }

    @ApiOperation("update")
    @PutMapping("")
    public boolean update(@RequestBody PeopleDTO dto) {
        People people = new People();
        BeanUtils.copyProperties(dto, people);

        return peopleService.updateById(people);
    }

    @ApiOperation("detail")
    @GetMapping("detail/{id}")
    public People detail(@PathVariable String id) {
        return peopleService.getById(id);
    }

    @ApiOperation("list")
    @GetMapping("list")
    public List<People> list(People people) {
        return peopleService.list(people);
    }

    // @ApiOperation("")
    // @GetMapping("page")
    // public List<People> page(IPage page, People people) {
    //     return peopleService.page(page);
    // }

    @ApiOperation("getByName")
    @GetMapping("getByName")
    public People getByName(String name) {
        return peopleService.getByName(name);
    }
}

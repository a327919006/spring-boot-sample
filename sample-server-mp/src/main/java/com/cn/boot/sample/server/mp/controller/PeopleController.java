package com.cn.boot.sample.server.mp.controller;

import com.cn.boot.sample.dal.mp.model.po.People;
import com.cn.boot.sample.server.mp.service.IPeopleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("")
    @GetMapping("save")
    public Long save(String name) {
        People people = new People();
        people.setName(name);
        peopleService.save(people);

        return people.getId();
    }


    @ApiOperation("")
    @GetMapping("test")
    public People getByName(String name) {
        return peopleService.getByName(name);
    }
}

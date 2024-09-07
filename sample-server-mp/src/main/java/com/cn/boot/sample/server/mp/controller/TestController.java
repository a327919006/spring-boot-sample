package com.cn.boot.sample.server.mp.controller;

import com.cn.boot.sample.dal.mp.entity.People;
import com.cn.boot.sample.server.mp.service.PeopleServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    @Autowired
    private PeopleServiceImpl peopleService;

    @ApiOperation("")
    @GetMapping("")
    @Transactional
    public Long save(String name) {
        People people = new People();
        people.setName(name);
        peopleService.insert(people);

        return people.getId();
    }
}

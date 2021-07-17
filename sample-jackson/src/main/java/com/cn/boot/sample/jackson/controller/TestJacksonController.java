package com.cn.boot.sample.jackson.controller;

import com.cn.boot.sample.jackson.entity.TestData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "jackson测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestJacksonController {

    @Autowired
    private ObjectMapper objectMapper;

    @ApiOperation("Obj转jsonStr")
    @GetMapping("o2j")
    public String o2j() throws JsonProcessingException {
        TestData data = new TestData();
        data.setId(System.currentTimeMillis());
        data.setName("test1");
        data.setCreateTime(LocalDateTime.now());
        return objectMapper.writeValueAsString(data);
    }


    @ApiOperation("jsonStr转Obj")
    @GetMapping("j2o")
    public TestData j2o() throws JsonProcessingException {
        TestData data = new TestData();
        data.setId(System.currentTimeMillis());
        data.setName("test2");
        data.setCreateTime(LocalDateTime.now());
        String json = objectMapper.writeValueAsString(data);

        return objectMapper.readValue(json, TestData.class);
    }
}

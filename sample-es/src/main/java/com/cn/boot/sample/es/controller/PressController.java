package com.cn.boot.sample.es.controller;

import com.cn.boot.sample.es.model.dto.StudentAddReq;
import com.cn.boot.sample.es.util.ElasticsearchUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/press")
@Api(tags = "压力测试(原生API)", produces = MediaType.APPLICATION_JSON_VALUE)
public class PressController {

    private static final String INDEX = "student";

    @Autowired
    private ElasticsearchUtil elasticsearchUtil;

    @ApiOperation("测试-添加")
    @PostMapping("")
    public boolean insert(int count, int skip) {
        StudentAddReq req = new StudentAddReq();
        for (int i = skip; i < count + skip; i++) {
            req.setId("" + i);
            req.setName("name-" + i);
            req.setAge(i);
            req.setCreateTime(LocalDateTime.now());
            boolean result = elasticsearchUtil.save(INDEX, req.getId(), req);
            log.info("index={}, result={}", i, result);
        }
        return true;
    }

    @ApiOperation("测试-更新")
    @PutMapping("")
    public boolean update(int count, int skip) {
        StudentAddReq req = new StudentAddReq();
        for (int i = skip; i < count + skip; i++) {
            req.setId("" + i);
            req.setName("name-" + i);
            req.setAge(i);
            req.setCreateTime(LocalDateTime.now());
            boolean result = elasticsearchUtil.update(INDEX, req);
            log.info("index={}, result={}", i, result);
        }
        return true;
    }
}

package com.cn.boot.sample.elasticsearch.controller;

import com.cn.boot.sample.api.model.po.User;
import com.cn.boot.sample.elasticsearch.util.ElasticsearchUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/origin")
@Api(tags = "原生API", produces = MediaType.APPLICATION_JSON_VALUE)
public class OriginApiController {

    @Autowired
    private ElasticsearchUtils elasticsearchUtils;

    @ApiOperation("用户-添加")
    @PostMapping("")
    public int insert(@RequestBody User req) {
        return 0;
    }

    @ApiOperation("用户-列表")
    @GetMapping("")
    public List<User> list(@ModelAttribute User req) {
        return null;
    }
}

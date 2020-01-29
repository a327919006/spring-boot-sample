package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.po.User;
import com.cn.boot.sample.api.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Reference
    private UserService userService;

    @ApiOperation("用户-添加")
    @PostMapping("")
    public int insert(@RequestBody User req) {
        return userService.insertSelective(req);
    }

    @ApiOperation("用户-列表")
    @GetMapping("")
    public List<User> list(@ModelAttribute User req) {
        return userService.list(req);
    }
}

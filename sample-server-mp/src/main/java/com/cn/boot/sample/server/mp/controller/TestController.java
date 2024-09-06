package com.cn.boot.sample.server.mp.controller;

import cn.hutool.core.util.IdUtil;
import com.cn.boot.sample.api.model.po.User;
import com.cn.boot.sample.api.service.UserService;
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
    private UserService userService;

    @ApiOperation("")
    @GetMapping("")
    @Transactional
    public String save(String name) {
        User user = new User();
        user.setId(IdUtil.getSnowflakeNextIdStr());
        user.setUsername(name);
        user.setPassword("123456");
        userService.insert(user);

        return user.getId();
    }
}

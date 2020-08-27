package com.cn.boot.sample.logback.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.logback.executor.AddExecutor;
import com.cn.boot.sample.logback.executor.NotifyExecutor;
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
@RequestMapping("/test")
@Api(tags = "测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    @Autowired
    private AddExecutor addExecutor;

    @Autowired
    private NotifyExecutor notifyExecutor;

    @ApiOperation("测试")
    @GetMapping("")
    public String list() {
        log.info("list start");
        addExecutor.execute(() -> {
            log.info("add start");

            notifyExecutor.execute(() -> {
                log.info("notify start");
                ThreadUtil.sleep(1000);
                log.info("notify success");
            });
            log.info("add success");
        });
        log.info("list success");
        return Constants.MSG_SUCCESS;
    }
}

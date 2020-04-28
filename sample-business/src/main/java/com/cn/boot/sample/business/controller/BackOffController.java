package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.BackOffExecution;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/backoff")
@Api(tags = "退避", produces = MediaType.APPLICATION_JSON_VALUE)
public class BackOffController {

    @ApiOperation("测试")
    @GetMapping("")
    public String list() {
        BackOff backOff = new FixedBackOff(1000, 3);
        BackOffExecution execution = backOff.start();

        execution.nextBackOff();
        return Constants.MSG_SUCCESS;
    }
}

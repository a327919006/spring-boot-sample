package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.dto.alert.AlertDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/alert")
@Api(tags = "测试19-AlertManager WebHook", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlertmanagerController {

    @ApiOperation("用于接收AlertManager告警信息")
    @PostMapping("")
    public void insert(@RequestBody AlertDTO req) {
        log.info("req = {}", req);
    }

}

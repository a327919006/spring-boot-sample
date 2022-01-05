package com.cn.boot.sample.iotdb.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.pool.SessionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    @Autowired
    private SessionPool sessionPool;

    @ApiOperation("商户-添加")
    @PostMapping("")
    public String insert(String deviceId, List<String> measurements, List<String> values) throws StatementExecutionException, IoTDBConnectionException {
        sessionPool.insertRecord(deviceId, System.currentTimeMillis(), measurements, values);
        return "SUCCESS";
    }
}

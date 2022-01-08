package com.cn.boot.sample.iotdb.controller;

import com.cn.boot.sample.iotdb.model.dto.IotDataDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.pool.SessionDataSetWrapper;
import org.apache.iotdb.session.pool.SessionPool;
import org.apache.iotdb.tsfile.read.common.Field;
import org.apache.iotdb.tsfile.read.common.RowRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/session")
@Api(tags = "测试session", produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionController {

    @Autowired
    private SessionPool sessionPool;

    @ApiOperation("insert")
    @PostMapping("")
    public String insert(@RequestBody IotDataDTO data) throws StatementExecutionException, IoTDBConnectionException {
        sessionPool.insertRecord(data.getDeviceId(), System.currentTimeMillis(),
                data.getMeasurements(), data.getValues());
        return "SUCCESS";
    }

    @ApiOperation("list")
    @GetMapping("")
    public String list(@ModelAttribute IotDataDTO data) throws StatementExecutionException, IoTDBConnectionException {
        List<String> list = Collections.singletonList(data.getDeviceId());
        long end = System.currentTimeMillis();
        Long before = data.getBefore();
        if (before == null) {
            before = 1000 * 60 * 60L;
        }
        long start = end - before;
        SessionDataSetWrapper result = sessionPool.executeRawDataQuery(list, start, end);
        RowRecord row;
        while ((row = result.next()) != null) {
            long timestamp = row.getTimestamp();
            log.info("time={}", timestamp);
            for (Field field : row.getFields()) {
                String value = field.getStringValue();
                log.info("value={}", value);
            }
        }
        sessionPool.closeResultSet(result);
        return "SUCCESS";
    }
}

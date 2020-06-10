package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.model.po.Room;
import com.cn.boot.sample.api.model.po.SensitiveWord;
import com.cn.boot.sample.api.model.po.Teacher;
import com.cn.boot.sample.api.service.DynamicDataSourceService;
import com.cn.boot.sample.api.service.SensitiveWordService;
import com.cn.boot.sample.api.service.RoomService;
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
@RequestMapping("/dynamic/ds")
@Api(tags = "动态数据源测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class DynamicDataSourceController {

    @Reference
    private SensitiveWordService sensitiveWordService;
    @Reference
    private DynamicDataSourceService dynamicDataSourceService;
    @Reference
    private RoomService roomService;

    @ApiOperation(value = "添加数据-mybatis", notes = "appId为数据源key,传入1或2，传入其他则为默认数据源1")
    @PostMapping("/data")
    public int insertData(@RequestBody SensitiveWord req) {
        return sensitiveWordService.insertSensitiveWord(req.getAppId(), req);
    }

    @ApiOperation(value = "添加数据-jpa", notes = "appId为数据源key,传入1或2，传入其他则为默认数据源1")
    @PostMapping("/jpa/data")
    public int insertJpaData(@RequestBody Room req) {
        roomService.insertRoom(req.getAppId(), req);
        return 1;
    }

    @ApiOperation(value = "添加数据，事务", notes = "appId为数据源key，传入3会触发事务回滚")
    @PostMapping("/data/transaction")
    public int insertDataWithTransaction(@RequestBody SensitiveWord req) {
        return sensitiveWordService.insertWithTransaction(req.getAppId(), req);
    }

    @ApiOperation("获取数据列表")
    @GetMapping("/data")
    public List<SensitiveWord> listData(@ModelAttribute SensitiveWord req) {
        return sensitiveWordService.list(req);
    }

    @ApiOperation(value = "添加数据源")
    @PostMapping("")
    public String addDataSource(String appId, String url, String username, String password) {
        String key = "ds" + appId;
        dynamicDataSourceService.addDataSource(key, url, username, password);
        return Constants.MSG_SUCCESS;
    }
}

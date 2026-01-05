package com.cn.boot.sample.server.controller;

import cn.hutool.core.util.IdUtil;
import com.cn.boot.sample.api.model.dto.RspBase;
import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.ClientService;
import com.cn.boot.sample.api.service.UidGeneratorService;
import com.cn.boot.sample.server.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    @Reference
    private ClientService clientService;
    @Autowired
    private TestService testService;

    @ApiOperation("测试事务注解是否生效")
    @GetMapping("")
    @Transactional
    public String save(String name) {
        Client client = new Client();
        client.setId(IdUtil.getSnowflakeNextIdStr());
        client.setName(name);
        client.setPlatId(name);
        clientService.saveClient(client);

        return client.getId();
    }

    @ApiOperation("测试流式查询")
    @GetMapping("list")
    public RspBase<String> list() {
        testService.listByHandler();
        return RspBase.success();
    }

    @GetMapping("/export/csv")
    @ApiOperation("测试流式导出csv")
    public void exportCsv(HttpServletResponse response) {
        testService.exportCsv(response);
    }

    @GetMapping("/export/excel")
    @ApiOperation("测试流式导出excel")
    public void exportExcel(HttpServletResponse response) {
        testService.exportExcel(response);
    }

}

package com.cn.boot.sample.server.controller;

import cn.hutool.core.util.IdUtil;
import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.ClientService;
import com.cn.boot.sample.api.service.UidGeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
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

    @Reference
    private ClientService clientService;

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
}

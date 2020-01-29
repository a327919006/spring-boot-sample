package com.cn.boot.sample.security.controller;

import com.cn.boot.sample.api.exceptions.BusinessException;
import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.model.vo.client.ClientGetRsp;
import com.cn.boot.sample.api.service.ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/client")
@Api(tags = "商户管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {

    @Reference
    private ClientService clientService;

    @ApiOperation("商户-获取")
    @GetMapping("/{id}")
    public ClientGetRsp get(@PathVariable String id) {
        Client client = clientService.selectByPrimaryKey(id);
        if (client == null) {
            throw new BusinessException("商户不存在");
        }

        ClientGetRsp rsp = new ClientGetRsp();
        BeanUtils.copyProperties(client, rsp);
        return rsp;
    }
}

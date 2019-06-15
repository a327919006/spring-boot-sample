package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.dto.client.ClientSearchReq;
import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.IClientService;
import com.cn.boot.sample.business.async.ClientQueue;
import com.cn.boot.sample.business.async.DeferredResultHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.util.concurrent.Callable;

/**
 * @author Chen Nan
 */
@RestController
@Api(tags = "异步接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequestMapping(value = "/async")
@Slf4j
public class AsyncController {

    @Reference
    private IClientService clientService;
    @Autowired
    private DeferredResultHolder resultHolder;
    @Autowired
    private ClientQueue clientQueue;

    @ApiOperation(value = "使用Callback")
    @GetMapping(value = "test1")
    public Callable<Client> test1(@ModelAttribute @Valid ClientSearchReq req) {
        log.info("【异步】开始：" + req);

        Callable<Client> result = () -> {
            log.info("开始获取商户");
            Client client = clientService.selectByPrimaryKey(req.getId());
            log.info("获取商户成功");
            return client;
        };

        log.info("【异步】成功");
        return result;
    }

    @ApiOperation(value = "使用Callback")
    @GetMapping(value = "test2")
    public DeferredResult<Client> test2(@ModelAttribute @Valid ClientSearchReq req) {
        log.info("【异步】开始：" + req);

        clientQueue.setClientId(req.getId());

        DeferredResult<Client> result = new DeferredResult<>();
        resultHolder.getMap().put(req.getId(), result);

        log.info("【异步】成功");
        return result;
    }
}

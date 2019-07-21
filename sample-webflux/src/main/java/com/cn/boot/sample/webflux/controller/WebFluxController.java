package com.cn.boot.sample.webflux.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.cn.boot.sample.api.exceptions.BusinessException;
import com.cn.boot.sample.api.model.dto.client.ClientListReq;
import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.model.vo.client.ClientGetRsp;
import com.cn.boot.sample.api.service.ClientService;
import com.cn.boot.sample.api.service.UidGeneratorService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/client")
@Api(tags = "商户管理", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WebFluxController {

    @Reference
    private ClientService clientService;
    @Reference
    private UidGeneratorService uidGeneratorService;

    @ApiOperation("sync获取数据")
    @GetMapping("/sync/data")
    public String getSyncData() {
        return getData("sync");
    }

    @ApiOperation("async获取数据")
    @GetMapping("/async/data")
    public Mono<String> getAsyncData() {
        return Mono.fromSupplier(() -> getData("async"));
    }

    @ApiOperation("async获取数组")
    @GetMapping("/async/array")
    public Flux<String> getAsyncArray() {
        String[] array = {
                getData("data1"),
                getData("data2"),
                getData("data3"),
        };
        return Flux.fromArray(array);
    }

    @ApiOperation("async获取列表")
    @GetMapping("/async/list")
    public Flux<String> getAsyncList() {
        List<String> list = new ArrayList<>();
        list.add("data1");
        list.add("data2");
        list.add("data3");
        return Flux.fromStream(list.stream().map(this::getData));
    }

    @ApiOperation(value = "sse", produces = "test/event-stream")
    @GetMapping("/sse/list")
    public Flux<String> getSseList() {
        List<String> list = new ArrayList<>();
        list.add("data1");
        list.add("data2");
        list.add("data3");
        return Flux.fromStream(list.stream());
    }

    @ApiOperation("商户-获取")
    @GetMapping("/{id}")
    public Mono<ClientGetRsp> get(@PathVariable String id) {
        Client client = clientService.selectByPrimaryKey(id);
        if (client == null) {
            throw new BusinessException("商户不存在");
        }

        ClientGetRsp rsp = new ClientGetRsp();
        BeanUtils.copyProperties(client, rsp);

        // Mono表示0-1个元素的异步序列
        return Mono.just(rsp);
    }

    @ApiOperation("商户-列表")
    @GetMapping("")
    public List<Client> list(@ModelAttribute ClientListReq req) {
        Page<Client> page = clientService.listPage(req);
        return page.getResult();
    }


    public String getData(String msg) {
        ThreadUtil.sleep(1000);
        return msg;
    }
}

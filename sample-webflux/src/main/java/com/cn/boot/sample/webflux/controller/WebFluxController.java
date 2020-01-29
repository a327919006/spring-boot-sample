package com.cn.boot.sample.webflux.controller;

import cn.hutool.core.thread.ThreadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebFluxController {

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

    @ApiOperation("事件流方式")
    @GetMapping(value = "/sse/list", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getSseList() {
        List<String> list = new ArrayList<>();
        list.add("data1");
        list.add("data2");
        list.add("data3");
        return Flux.fromStream(list.stream());
    }

    /**
     * 模拟异步获取数据
     */
    public String getData(String msg) {
        ThreadUtil.sleep(1000);
        return msg;
    }
}

package com.cn.boot.sample.hazelcast.controller;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 计数器功能，需在HazelcastConfig中配置config.getCPSubsystemConfig().setCPMemberCount(3);
 * 并启动3个节点
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/atomic")
@Api(tags = "计数器", produces = MediaType.APPLICATION_JSON_VALUE)
public class AtomicController {

    @Autowired
    private HazelcastInstance hzInstance;

    @ApiOperation("保存数据")
    @PostMapping("/{name}/increment")
    public long set(@PathVariable String name) {
        IAtomicLong atomicLong = hzInstance.getCPSubsystem().getAtomicLong(name);
        return atomicLong.incrementAndGet();
    }

    @ApiOperation("获取")
    @GetMapping("/{name}")
    public long get(@PathVariable String name) {
        IAtomicLong atomicLong = hzInstance.getCPSubsystem().getAtomicLong(name);
        return atomicLong.get();
    }
}

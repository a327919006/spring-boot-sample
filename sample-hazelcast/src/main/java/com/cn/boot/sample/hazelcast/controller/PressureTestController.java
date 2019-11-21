package com.cn.boot.sample.hazelcast.controller;

import cn.hutool.core.util.IdUtil;
import com.cn.boot.sample.api.model.Constants;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/pressure/test")
@Api(tags = "压力测试", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PressureTestController {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    /**
     * 单map写入速率7W/s 4G内存 最大1100W左右个数据(可能还由本机CPU/内存有关)
     *
     * @param dataCount 写入数据数量
     * @param printStep 打印日志步长
     */
    @ApiOperation("单map压力测试")
    @PostMapping("/map/single")
    public String singleMap(int dataCount, int printStep) {
        long start = System.currentTimeMillis();
        long temp = System.currentTimeMillis();
        String key = IdUtil.simpleUUID();
        IMap<String, String> dataMap = hazelcastInstance.getMap("MapPressureTest");
        for (int i = 0; i < dataCount; i++) {
            dataMap.put(key + i, key);
            if (0 == i % printStep) {
                log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                temp = System.currentTimeMillis();
            }
        }
        log.info("总耗时:{}", System.currentTimeMillis() - start);
        return Constants.MSG_SUCCESS;
    }


    /**
     * 新map创建写入速率2W/s 4G内存 最大25W左右个map(可能还由本机CPU/内存有关)
     *
     * @param dataCount 写入数据数量
     * @param printStep 打印日志步长
     */
    @ApiOperation("map压力测试")
    @PostMapping("/map")
    public String set(int dataCount, int printStep) {
        long start = System.currentTimeMillis();
        long temp = System.currentTimeMillis();
        String key = IdUtil.simpleUUID();
        for (int i = 0; i < dataCount; i++) {
            String name = key + i;
            IMap<String, String> dataMap = hazelcastInstance.getMap(name);
            dataMap.put(key + i, key);
            if (0 == i % printStep) {
                log.info("i = {}, time={}", i, System.currentTimeMillis() - temp);
                temp = System.currentTimeMillis();
            }
        }
        log.info("总耗时:{}", System.currentTimeMillis() - start);
        return Constants.MSG_SUCCESS;
    }
}

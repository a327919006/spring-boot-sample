package com.cn.boot.sample.zookeeper.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.zookeeper.curator.CuratorUtil;
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
@RequestMapping("/curator")
@Api(tags = "Curator测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class CuratorController {

    @Autowired
    private CuratorUtil curatorUtil;

    @ApiOperation("创建临时节点")
    @PostMapping("/node/ephemeral")
    public String set(String node) throws Exception {
        curatorUtil.createEphemeral(node);
        return Constants.MSG_SUCCESS;
    }
}

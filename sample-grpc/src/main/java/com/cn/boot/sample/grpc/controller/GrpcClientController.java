package com.cn.boot.sample.grpc.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.grpc.config.GrpcClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/grpc/client")
@Api(tags = "grpc客户端测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrpcClientController {

    @Autowired
    private GrpcClient grpcClient;

    @ApiOperation("HelloWorld")
    @PostMapping("")
    public String insert(@RequestBody String name) {
        grpcClient.greet(name);
        return Constants.MSG_SUCCESS;
    }

}

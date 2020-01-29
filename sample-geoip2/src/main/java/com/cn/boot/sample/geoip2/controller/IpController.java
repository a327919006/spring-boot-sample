package com.cn.boot.sample.geoip2.controller;

import com.cn.boot.sample.geoip2.model.AddressRsp;
import com.cn.boot.sample.geoip2.service.IpService;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/ip")
@Api(tags = "IP管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class IpController {

    @Autowired
    private IpService ipService;

    @ApiOperation("获取地址")
    @GetMapping
    public AddressRsp send(String ip) throws IOException, GeoIp2Exception {
        return ipService.printIpAddress(ip);
    }
}

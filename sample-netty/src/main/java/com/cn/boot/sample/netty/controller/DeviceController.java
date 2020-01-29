package com.cn.boot.sample.netty.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.netty.server.ClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@RestController
@RequestMapping("/device")
@Api(tags = "设备", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeviceController {

    @ApiOperation("发送")
    @PostMapping("/send/{deviceNo}")
    public String send(@PathVariable String deviceNo, String data) {
        boolean result = ClientUtils.sendData(deviceNo, data);
        if (result) {
            return Constants.MSG_SUCCESS;
        }
        return Constants.MSG_FAIL;
    }

    @ApiOperation("离线")
    @PostMapping("/offline/{deviceNo}")
    public String offline(@PathVariable String deviceNo) {
        boolean result = ClientUtils.offline(deviceNo);
        if (result) {
            return Constants.MSG_SUCCESS;
        }
        return Constants.MSG_FAIL;
    }
}

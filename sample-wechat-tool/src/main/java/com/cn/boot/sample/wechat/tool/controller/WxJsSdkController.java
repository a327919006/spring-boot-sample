package com.cn.boot.sample.wechat.tool.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@RestController
@RequestMapping("/wx/jssdk")
@Api(tags = "微信二维码", produces = MediaType.APPLICATION_JSON_VALUE)
public class WxJsSdkController {

    @Autowired
    private WxMpService wxService;

    @ApiOperation(value = "获取jssdk配置")
    @GetMapping("/config")
    public WxJsapiSignature sendTemplateMsg(String url) throws WxErrorException {
        return wxService.createJsapiSignature(url);
    }
}

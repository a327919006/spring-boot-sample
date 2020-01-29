package com.cn.boot.sample.wechat.tool.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@RestController
@RequestMapping("/wx/qrcode")
@Api(tags = "微信二维码", produces = MediaType.APPLICATION_JSON_VALUE)
public class WxQrCodeController {

    @Autowired
    private WxMpService wxService;

    @ApiOperation(value = "创建二维码Ticket")
    @PostMapping("/ticket")
    public WxMpQrCodeTicket sendTemplateMsg(String sceneStr, Integer expireSeconds) {
        try {
            return wxService.getQrcodeService().qrCodeCreateTmpTicket(sceneStr, expireSeconds);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.cn.boot.sample.wechat.tool.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Chen Nan
 */
@RestController
@RequestMapping("/wx/message/template")
@Api(tags = "微信模板消息", produces = MediaType.APPLICATION_JSON_VALUE)
public class WxTemplateMsgController {

    @Autowired
    private WxMpService wxService;

    @ApiOperation(value = "发送模板消息")
    @PostMapping("/send")
    public String sendTemplateMsg(@RequestBody WxMpTemplateMessage req) {
        try {
            return wxService.getTemplateMsgService().sendTemplateMsg(req);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return "error";
    }
}

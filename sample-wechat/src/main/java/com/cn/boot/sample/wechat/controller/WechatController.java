package com.cn.boot.sample.wechat.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.wechat.model.BaseMsgRsp;
import com.cn.boot.sample.wechat.model.CheckMsgDTO;
import com.cn.boot.sample.wechat.model.ReceiveMsgDTO;
import com.cn.boot.sample.wechat.service.WechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("wx")
@Api(tags = "微信", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WechatController {

    @Autowired
    private WechatService wechatService;

    @ApiOperation("验证消息的确来自微信服务器")
    @GetMapping
    public String checkMsg(@ModelAttribute @Valid CheckMsgDTO req) {
        log.info("验证消息,req={}", req);

        if (wechatService.check(req)) {
            log.info("验证成功");
            return req.getEchostr();
        } else {
            log.error("验证失败");
            return Constants.MSG_FAIL;
        }
    }

    @ApiOperation(value = "接收消息", produces = MediaType.APPLICATION_XML_VALUE)
    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public BaseMsgRsp receiveMsg(@RequestBody ReceiveMsgDTO req) {
        BaseMsgRsp baseMsgRsp = wechatService.handleMsg(req);
        log.info("rsp={}", baseMsgRsp);
        return baseMsgRsp;
    }
}

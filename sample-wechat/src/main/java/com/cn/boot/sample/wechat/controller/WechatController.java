package com.cn.boot.sample.wechat.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.wechat.model.*;
import com.cn.boot.sample.wechat.service.WechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("wx")
@Api(tags = "微信", produces = MediaType.APPLICATION_JSON_VALUE)
public class WechatController {

    @Autowired
    private WechatService wechatService;

    @ApiOperation("验证消息的确来自微信服务器")
    @GetMapping("/portal")
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
    @PostMapping(value = "/portal", produces = MediaType.APPLICATION_XML_VALUE)
    public BaseMsgRsp receiveMsg(@RequestBody ReceiveMsgDTO req) {
        BaseMsgRsp baseMsgRsp = wechatService.handleMsg(req);
        log.info("rsp={}", baseMsgRsp);
        return baseMsgRsp;
    }

    @ApiOperation(value = "发送模板消息")
    @PostMapping("/message/template/send")
    public long sendTemplateMsg(@RequestBody SendTemplateMsgDTO req) {
        return wechatService.sendTemplateMsg(req);
    }

    @ApiOperation(value = "上传临时素材")
    @PostMapping("/media/upload")
    public String uploadMedia(MultipartFile media) throws IOException {
        return wechatService.uploadMedia(media.getBytes(), "image", media.getOriginalFilename());
    }

    @ApiOperation(value = "创建二维码Ticket")
    @PostMapping("/qrcode/ticket")
    public String createQrCodeTicket(@RequestBody CreateQrCodeTicketDTO req) {
        return wechatService.createQrCodeTicket(req);
    }

    @ApiOperation(value = "用户授权回调")
    @GetMapping("/code/callback")
    public Object callback(@ModelAttribute CodeCallbackDTO req) {
        String code = req.getCode();
        UserTokenRsp token = wechatService.getUserTokenByCode(code);
        if (StringUtils.endsWithIgnoreCase(req.getState(), "userinfo")) {
            UserInfoRsp userInfoByToken = wechatService.getUserInfoByToken(token.getAccess_token(), token.getOpenid());
            return userInfoByToken;
        }

        return token.getOpenid();
    }
}

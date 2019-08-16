package com.cn.boot.sample.wechat.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.cn.boot.sample.api.util.ChatRobotUtil;
import com.cn.boot.sample.wechat.config.properties.WechatProperties;
import com.cn.boot.sample.wechat.model.*;
import com.cn.boot.sample.wechat.service.WechatService;
import com.cn.boot.sample.wechat.util.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    @Autowired
    public WechatProperties wechatProperties;
    @Autowired
    public WechatUtils wechatUtils;

    @Override
    public boolean check(CheckMsgDTO req) {
        // 1）将token、timestamp、nonce三个参数进行字典序排序
        String[] args = new String[]{wechatProperties.getToken(), req.getTimestamp(), req.getNonce()};
        Arrays.sort(args);

        // 2）将三个参数字符串拼接成一个字符串进行sha1加密
        String data = args[0] + args[1] + args[2];
        String sign = SecureUtil.sha1(data);
        log.info("sign={}", sign);

        // 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        return StringUtils.equalsIgnoreCase(req.getSignature(), sign);
    }

    @Override
    public BaseMsgRsp handleMsg(ReceiveMsgDTO req) {
        // 消息类型，文本为text、图片为image、语音为voice、视频为video、小视频为shortvideo、位置为location、连接为link
        switch (req.getMsgType()) {
            case "text":
                if (StringUtils.containsIgnoreCase(req.getContent(), "登录")) {
                    return handleLoginMsg(req);
                }
                // 调用聊天机器人
                String chat = ChatRobotUtil.chat(req.getContent());
                if (StringUtils.isNotBlank(chat)) {
                    return new TextMsgRsp(req, chat);
                }
                return new TextMsgRsp(req, "收到:" + req.getContent());
            case "image":
                List<ArticleMsgRsp> articles = new ArrayList<>();
                articles.add(new ArticleMsgRsp("跳转到百度", "描述信息", req.getPicUrl(), "http://baidu.com"));
                return new NewsMsgRsp(req, articles);
            case "event":
                return handleEventMsg(req);
            default:
                return new TextMsgRsp(req, "暂不支持该类型消息:" + req.getMsgType());
        }
    }

    @Override
    public String getAccessToken() {
        return wechatUtils.getAccessToken();
    }

    @Override
    public void createMenu(CreateMenuDTO req) {
        wechatUtils.createMenu(req);
    }

    @Override
    public void updateIndustry(UpdateIndustryDTO req) {
        wechatUtils.updateIndustry(req);
    }

    @Override
    public long sendTemplateMsg(SendTemplateMsgDTO req) {
        return wechatUtils.sendTemplateMsg(req);
    }

    @Override
    public String uploadMedia(byte[] fileBytes, String type, String fileName) {
        return wechatUtils.uploadMedia(fileBytes, type, fileName);
    }

    @Override
    public String createQrCodeTicket(CreateQrCodeTicketDTO req) {
        return wechatUtils.createQrCodeTicket(req);
    }

    @Override
    public UserInfoRsp getUserInfo(String openId) {
        return wechatUtils.getUserInfo(openId);
    }

    @Override
    public UserTokenRsp getUserTokenByCode(String code) {
        return wechatUtils.getUserTokenByCode(code);
    }

    @Override
    public UserInfoRsp getUserInfoByToken(String accessToken, String openid) {
        return wechatUtils.getUserInfoByToken(accessToken, openid);
    }

    /**
     * 处理事件消息
     *
     * @param req 消息内容
     * @return 响应消息
     */
    private BaseMsgRsp handleEventMsg(ReceiveMsgDTO req) {
        switch (req.getEvent()) {
            case "CLICK":
                return new TextMsgRsp(req, "点击了:" + req.getEventKey());
            case "TEMPLATESENDJOBFINISH":
                log.info("模板消息回调，msgID={},status={}", req.getMsgID(), req.getStatus());
                return null;
            case "SCAN":
                log.info("扫描二维码回调，eventKey={}", req.getEventKey());
                return new TextMsgRsp(req, "扫描了二维码，内容:" + req.getEventKey());
            case "subscribe":
                if (StringUtils.isBlank(req.getEventKey())) {
                    log.info("用户关注，eventKey={}", req.getEventKey());
                    return new TextMsgRsp(req, "欢迎关注");
                } else {
                    log.info("用户扫二维码关注，eventKey={}", req.getEventKey());
                    String qrContent = StringUtils.remove(req.getEventKey(), "qrscene_");
                    return new TextMsgRsp(req, "扫二维码关注，内容:" + qrContent);
                }
            case "unsubscribe":
                log.info("用户取关，openId={}", req.getFromUserName());
                return null;
            default:
                return new TextMsgRsp(req, "暂不支持该类型事件:" + req.getEvent());
        }
    }

    /**
     * 处理登录消息
     *
     * @param req 消息内容
     * @return 响应消息
     */
    private BaseMsgRsp handleLoginMsg(ReceiveMsgDTO req) {
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
        String param = "appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE";
        String redirectUri = "http://cntest.free.idcfengye.com/sample-wechat/wx/code/callback";

        if (StringUtils.containsIgnoreCase(req.getContent(), "静默")) {
            param = param.replace("SCOPE", "snsapi_base");
            param = param.replace("STATE", "base");
        }

        param = param.replace("APPID", wechatProperties.getAppId())
                .replace("REDIRECT_URI", redirectUri)
                .replace("SCOPE", "snsapi_userinfo")
                .replace("STATE", "userinfo");

        param = HttpUtil.encodeParams(param, StandardCharsets.UTF_8);

        url += param + "#wechat_redirect";
        return new TextMsgRsp(req, "点击<a href=\"" + url + "\">这里</a>登录");
    }
}

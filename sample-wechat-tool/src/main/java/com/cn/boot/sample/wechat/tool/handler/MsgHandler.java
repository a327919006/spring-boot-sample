package com.cn.boot.sample.wechat.tool.handler;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.api.util.ChatRobotUtil;
import com.cn.boot.sample.wechat.tool.builder.TextBuilder;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MsgHandler extends AbstractHandler {

    @Autowired
    private TextBuilder builder;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage req,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        switch (req.getMsgType()) {
            case XmlMsgType.TEXT:
                if (StringUtils.containsIgnoreCase(req.getContent(), "登录")) {
                    return handleLoginMsg(req, weixinService);
                }
                // 调用聊天机器人
                String chat = ChatRobotUtil.chat(req.getContent());
                if (StringUtils.isNotBlank(chat)) {
                    return builder.build(chat, req, weixinService);
                }

                String content = "收到：" + JSONUtil.toJsonStr(req);
                return builder.build(content, req, weixinService);
            case XmlMsgType.IMAGE:
                WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                item.setTitle("跳转到百度");
                item.setDescription("描述信息");
                item.setPicUrl(req.getPicUrl());
                item.setUrl("http://baidu.com");

                return WxMpXmlOutMessage.NEWS().addArticle(item)
                        .fromUser(req.getToUser()).toUser(req.getFromUser())
                        .build();
            default:
                return builder.build("暂不支持该类型消息:" + req.getMsgType(), req, weixinService);

        }
    }

    /**
     * 处理登录消息
     *
     * @param wxMessage 消息内容
     * @return 响应消息
     */
    private WxMpXmlOutMessage handleLoginMsg(WxMpXmlMessage wxMessage, WxMpService weixinService) {
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
        String param = "appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE";
        String redirectUri = "http://cntest.free.idcfengye.com/sample-wechat/wx/code/callback";

        if (StringUtils.containsIgnoreCase(wxMessage.getContent(), "静默")) {
            param = param.replace("SCOPE", "snsapi_base");
            param = param.replace("STATE", "base");
        }

        param = param.replace("APPID", weixinService.getWxMpConfigStorage().getAppId())
                .replace("REDIRECT_URI", redirectUri)
                .replace("SCOPE", "snsapi_userinfo")
                .replace("STATE", "userinfo");

        param = HttpUtil.encodeParams(param, StandardCharsets.UTF_8);

        url += param + "#wechat_redirect";
        return builder.build("点击<a href=\"" + url + "\">这里</a>登录", wxMessage, weixinService);
    }
}

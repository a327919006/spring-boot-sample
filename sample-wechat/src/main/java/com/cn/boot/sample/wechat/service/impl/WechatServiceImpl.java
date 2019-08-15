package com.cn.boot.sample.wechat.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.cn.boot.sample.wechat.config.properties.WechatProperties;
import com.cn.boot.sample.wechat.model.*;
import com.cn.boot.sample.wechat.service.WechatService;
import com.cn.boot.sample.wechat.util.WechatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                return new TextMsgRsp(req, "收到:" + req.getContent());
            case "image":
                List<ArticleMsgRsp> articles = new ArrayList<>();
                articles.add(new ArticleMsgRsp("跳转到百度", "描述信息", req.getPicUrl(), "http://baidu.com"));
                return new NewsMsgRsp(req, articles);
            case "event":
                return handleEventMsg(req);
            default:
                return new TextMsgRsp(req, "暂不支持该类型消息");
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

    private BaseMsgRsp handleEventMsg(ReceiveMsgDTO req) {
        switch (req.getEvent()){
            case "CLICK":
                return new TextMsgRsp(req, "点击了:" + req.getEventKey());
            default:
                return new TextMsgRsp(req, "暂不支持该类型消息");
        }
    }
}

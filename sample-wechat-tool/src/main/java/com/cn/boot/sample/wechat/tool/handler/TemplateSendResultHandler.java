package com.cn.boot.sample.wechat.tool.handler;

import com.cn.boot.sample.wechat.tool.builder.TextBuilder;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class TemplateSendResultHandler extends AbstractHandler {

    @Autowired
    private TextBuilder builder;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        log.info("模板消息回调，msgID={},status={}", wxMpXmlMessage.getMsgId(), wxMpXmlMessage.getStatus());
        return null;
    }
}

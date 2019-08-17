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
 * @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
@Component
public class ScanHandler extends AbstractHandler {

    @Autowired
    private TextBuilder builder;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        log.info("扫描二维码回调，eventKey={}", wxMpXmlMessage.getEventKey());
        return builder.build("扫描了二维码，内容:" + wxMpXmlMessage.getEventKey(), wxMpXmlMessage, wxMpService);
    }
}

package com.cn.boot.sample.design.pattern.facade;

/**
 * @author Chen Nan
 */
public class WechatMessageManager implements MessageManager {
    @Override
    public void pushMessage() {
        System.out.println("推送微信");
    }
}

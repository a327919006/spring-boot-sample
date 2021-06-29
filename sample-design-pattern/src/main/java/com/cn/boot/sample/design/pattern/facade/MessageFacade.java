package com.cn.boot.sample.design.pattern.facade;

/**
 *
 * @author Chen Nan
 */
public class MessageFacade implements MessageManager {
    private MessageManager sms = new SmsMessageManager();
    private MessageManager mail = new MailMessageManager();
    private MessageManager wechat = new WechatMessageManager();

    @Override
    public void pushMessage() {
        sms.pushMessage();
        mail.pushMessage();
        wechat.pushMessage();
    }
}

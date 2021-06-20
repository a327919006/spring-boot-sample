package com.cn.boot.sample.design.pattern.factory.method;

/**
 * @author Chen Nan
 */
public class WechatPayFactory implements PayFactory {
    @Override
    public Pay createPay() {
        return new WechatPay();
    }
}

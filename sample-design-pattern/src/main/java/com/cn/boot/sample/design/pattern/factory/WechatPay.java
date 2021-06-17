package com.cn.boot.sample.design.pattern.factory;

/**
 * @author Chen Nan
 */
public class WechatPay implements Pay {
    @Override
    public void unifiedOrder() {
        System.out.println("微信支付下单");
    }
}

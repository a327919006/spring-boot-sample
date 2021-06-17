package com.cn.boot.sample.design.pattern.factory.method;

import com.cn.boot.sample.design.pattern.factory.Pay;
import com.cn.boot.sample.design.pattern.factory.WechatPay;

/**
 * @author Chen Nan
 */
public class WechatPayFactory implements PayFactory {
    @Override
    public Pay createPay() {
        return new WechatPay();
    }
}

package com.cn.boot.sample.design.pattern.factory.abstrac.wechat;

import com.cn.boot.sample.design.pattern.factory.abstrac.OrderFactory;
import com.cn.boot.sample.design.pattern.factory.abstrac.Pay;
import com.cn.boot.sample.design.pattern.factory.abstrac.Refund;

/**
 * @author Chen Nan
 */
public class WechatOrderFactoryImpl implements OrderFactory {
    @Override
    public Pay createPay() {
        return new WechatPayImpl();
    }

    @Override
    public Refund createRefund() {
        return new WechatRefundImpl();
    }
}

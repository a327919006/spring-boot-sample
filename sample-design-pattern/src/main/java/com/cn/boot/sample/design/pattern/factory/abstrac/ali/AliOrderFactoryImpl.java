package com.cn.boot.sample.design.pattern.factory.abstrac.ali;

import com.cn.boot.sample.design.pattern.factory.abstrac.OrderFactory;
import com.cn.boot.sample.design.pattern.factory.abstrac.Pay;
import com.cn.boot.sample.design.pattern.factory.abstrac.Refund;

/**
 * @author Chen Nan
 */
public class AliOrderFactoryImpl implements OrderFactory {
    @Override
    public Pay createPay() {
        return new AliPayImpl();
    }

    @Override
    public Refund createRefund() {
        return new AliRefundImpl();
    }
}

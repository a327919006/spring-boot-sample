package com.cn.boot.sample.design.pattern.factory.method;

import com.cn.boot.sample.design.pattern.factory.AliPay;
import com.cn.boot.sample.design.pattern.factory.Pay;

/**
 * @author Chen Nan
 */
public class AliPayFactory implements PayFactory {
    @Override
    public Pay createPay() {
        return new AliPay();
    }
}

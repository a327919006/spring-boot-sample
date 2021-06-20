package com.cn.boot.sample.design.pattern.factory.method;

/**
 * @author Chen Nan
 */
public class AliPayFactory implements PayFactory {
    @Override
    public Pay createPay() {
        return new AliPay();
    }
}

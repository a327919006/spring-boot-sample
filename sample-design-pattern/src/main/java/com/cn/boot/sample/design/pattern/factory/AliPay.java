package com.cn.boot.sample.design.pattern.factory;

/**
 * @author Chen Nan
 */
public class AliPay implements Pay {
    @Override
    public void unifiedOrder() {
        System.out.println("支付宝支付下单");
    }
}

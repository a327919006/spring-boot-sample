package com.cn.boot.sample.design.pattern.factory.abstrac;

/**
 * 订单抽象工厂
 *
 * @author Chen Nan
 */
public interface OrderFactory {

    /**
     * 订单支付
     */
    Pay createPay();

    /**
     * 订单退款
     */
    Refund createRefund();
}

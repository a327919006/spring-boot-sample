package com.cn.boot.sample.design.pattern.factory.simple;

import com.cn.boot.sample.design.pattern.factory.method.AliPay;
import com.cn.boot.sample.design.pattern.factory.method.Pay;
import com.cn.boot.sample.design.pattern.factory.method.WechatPay;

/**
 * 简单工厂模式
 *
 * @author Chen Nan
 */
public class SimplePayFactory {

    /**
     * 根据支付类型返回支付对象
     *
     * @param payType
     * @return
     */
    public static Pay createPay(int payType) {
        if (0 == payType) {
            return new AliPay();
        } else if (1 == payType) {
            return new WechatPay();
        }
        return null;
    }
}

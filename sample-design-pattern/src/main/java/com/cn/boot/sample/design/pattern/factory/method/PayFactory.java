package com.cn.boot.sample.design.pattern.factory.method;

import com.cn.boot.sample.design.pattern.factory.Pay;

/**
 * 工厂方法模式
 * @author Chen Nan
 */
public interface PayFactory {
    Pay createPay();
}

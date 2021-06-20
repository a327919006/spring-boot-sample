package com.cn.boot.sample.design.pattern.factory.abstrac;

import com.cn.boot.sample.design.pattern.factory.abstrac.ali.AliOrderFactoryImpl;
import com.cn.boot.sample.design.pattern.factory.abstrac.wechat.WechatOrderFactoryImpl;

/**
 * @author Chen Nan
 */
public class FactoryProducer {

    public static OrderFactory getOrderFactory(int type) {
        if (0 == type) {
            return new AliOrderFactoryImpl();
        } else if (1 == type) {
            return new WechatOrderFactoryImpl();
        }
        return null;
    }

}

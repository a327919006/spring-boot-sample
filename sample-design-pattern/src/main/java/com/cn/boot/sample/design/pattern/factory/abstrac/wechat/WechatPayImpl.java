package com.cn.boot.sample.design.pattern.factory.abstrac.wechat;

import com.cn.boot.sample.design.pattern.factory.abstrac.Pay;

/**
 * @author Chen Nan
 */
public class WechatPayImpl implements Pay {
    @Override
    public void pay() {
        System.out.println("wechat pay");
    }
}

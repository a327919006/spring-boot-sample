package com.cn.boot.sample.design.pattern.factory.abstrac.wechat;

import com.cn.boot.sample.design.pattern.factory.abstrac.Refund;

/**
 * @author Chen Nan
 */
public class WechatRefundImpl implements Refund {
    @Override
    public void refund() {
        System.out.println("wechat refund");
    }
}

package com.cn.boot.sample.design.pattern.proxy;

/**
 * @author Chen Nan
 */
public class PhoneSellImpl implements PhoneSell {

    @Override
    public void sell() {
        System.out.println("手机销售");
    }
}

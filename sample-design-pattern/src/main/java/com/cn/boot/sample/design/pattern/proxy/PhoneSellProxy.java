package com.cn.boot.sample.design.pattern.proxy;

/**
 * @author Chen Nan
 */
public class PhoneSellProxy implements PhoneSell {

    private PhoneSell phoneSell = new PhoneSellImpl();

    @Override
    public void sell() {
        makeAddress();
        phoneSell.sell();
        makeAd();
    }

    private void makeAddress() {
        System.out.println("一个人流量很高的地址");
    }

    private void makeAd() {
        System.out.println("投放广告");
    }

}

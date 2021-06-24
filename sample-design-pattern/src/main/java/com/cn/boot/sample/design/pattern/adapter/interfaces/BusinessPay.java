package com.cn.boot.sample.design.pattern.adapter.interfaces;

/**
 * 不想实现一个接口中所有的方法时，可以创建一个Adapter，实现所有方法，在写别的类的时候，继承Adapter类即
 *
 * @author Chen Nan
 */
public class BusinessPay extends PayAdapter {

    @Override
    public void createOrder() {
        System.out.println("createOrder");
    }

    @Override
    public void pay() {
        System.out.println("pay");
    }
}

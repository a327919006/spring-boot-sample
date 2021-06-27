package com.cn.boot.sample.design.pattern.decorator;

/**
 * @author Chen Nan
 */
public class BigBike implements Bike {
    @Override
    public String getDescribe() {
        return "大号自行车";
    }

    @Override
    public int getPrice() {
        return 200;
    }
}

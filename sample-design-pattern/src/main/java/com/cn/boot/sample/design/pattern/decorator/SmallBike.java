package com.cn.boot.sample.design.pattern.decorator;

/**
 * @author Chen Nan
 */
public class SmallBike implements Bike {
    @Override
    public String getDescribe() {
        return "小号自行车";
    }

    @Override
    public int getPrice() {
        return 100;
    }
}

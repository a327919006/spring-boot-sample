package com.cn.boot.sample.design.pattern.decorator;

/**
 * @author Chen Nan
 */
public class SuonaDecorator extends BikeDecorator {
    private Bike bike;

    public SuonaDecorator(Bike bike) {
        this.bike = bike;
    }

    @Override
    public String getDescribe() {
        return bike.getDescribe() + ",加上喇叭";
    }

    @Override
    public int getPrice() {
        return bike.getPrice() + 30;
    }
}

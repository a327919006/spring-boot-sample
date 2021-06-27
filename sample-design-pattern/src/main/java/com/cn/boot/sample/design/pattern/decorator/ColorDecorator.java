package com.cn.boot.sample.design.pattern.decorator;

/**
 * @author Chen Nan
 */
public class ColorDecorator extends BikeDecorator {

    private Bike bike;

    public ColorDecorator(Bike bike) {
        this.bike = bike;
    }

    @Override
    public String getDescribe() {
        return bike.getDescribe() + ",加上彩色喷漆";
    }

    @Override
    public int getPrice() {
        return bike.getPrice() + 50;
    }
}

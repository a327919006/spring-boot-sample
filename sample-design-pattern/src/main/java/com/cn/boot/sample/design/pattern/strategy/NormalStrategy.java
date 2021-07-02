package com.cn.boot.sample.design.pattern.strategy;

/**
 * @author Chen Nan
 */
public class NormalStrategy implements PriceStrategy {
    @Override
    public double computePrice(double price) {
        System.out.println("没有折扣");
        return price;
    }
}

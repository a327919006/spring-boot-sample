package com.cn.boot.sample.design.pattern.strategy;

/**
 * @author Chen Nan
 */
public class DiscountStrategy implements PriceStrategy {
    @Override
    public double computePrice(double price) {
        System.out.println("打折");
        return price * 0.5;
    }
}

package com.cn.boot.sample.design.pattern.strategy;

/**
 * @author Chen Nan
 */
public class PriceContext {

    private PriceStrategy priceStrategy;

    public PriceContext(PriceStrategy priceStrategy) {
        this.priceStrategy = priceStrategy;
    }

    public double execute(double price) {
        return priceStrategy.computePrice(price);
    }
}

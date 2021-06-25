package com.cn.boot.sample.design.pattern.bridge;

/**
 * @author Chen Nan
 */
public class RedColor implements Color {
    @Override
    public void useColor() {
        System.out.println("红色");
    }
}

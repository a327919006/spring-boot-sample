package com.cn.boot.sample.design.pattern.bridge;

/**
 * @author Chen Nan
 */
public class YellowColor implements Color {
    @Override
    public void useColor() {
        System.out.println("黄色");
    }
}

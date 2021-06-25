package com.cn.boot.sample.design.pattern.bridge;

/**
 * @author Chen Nan
 */
public class ApplePhone extends Phone {

    public ApplePhone(Color color) {
        super.setColor(color);
    }

    @Override
    public void run() {
        color.useColor();
        System.out.println("苹果手机");
    }
}

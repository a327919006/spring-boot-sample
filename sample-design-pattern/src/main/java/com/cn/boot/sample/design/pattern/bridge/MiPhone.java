package com.cn.boot.sample.design.pattern.bridge;

/**
 * @author Chen Nan
 */
public class MiPhone extends Phone {

    public MiPhone(Color color) {
        super.setColor(color);
    }

    @Override
    public void run() {
        color.useColor();
        System.out.println("小米手机");
    }
}

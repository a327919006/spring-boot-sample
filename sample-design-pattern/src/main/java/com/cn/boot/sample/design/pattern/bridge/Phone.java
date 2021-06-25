package com.cn.boot.sample.design.pattern.bridge;

/**
 * @author Chen Nan
 */
public abstract class Phone {

    protected Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    public abstract void run();
}

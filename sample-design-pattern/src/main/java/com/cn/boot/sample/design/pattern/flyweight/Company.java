package com.cn.boot.sample.design.pattern.flyweight;

/**
 * @author Chen Nan
 */
public class Company {
    private String name;

    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

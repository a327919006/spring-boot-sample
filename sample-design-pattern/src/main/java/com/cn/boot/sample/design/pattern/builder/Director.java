package com.cn.boot.sample.design.pattern.builder;

/**
 * @author Chen Nan
 */
public class Director {

    public static Computer createComputer(Builder builder) {
        return builder.buildCpu()
                .buildDisk()
                .buildMemory()
                .buildPower()
                .create();
    }
}

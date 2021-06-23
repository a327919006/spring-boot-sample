package com.cn.boot.sample.design.pattern.builder;

/**
 * @author Chen Nan
 */
public interface Builder {

    Builder buildCpu();

    Builder buildMemory();

    Builder buildDisk();

    Builder buildPower();

    Computer create();
}

package com.cn.boot.sample.design.pattern.builder;

/**
 * @author Chen Nan
 */
public interface ComputerBuilder {

    ComputerBuilder buildCpu();

    ComputerBuilder buildMemory();

    ComputerBuilder buildDisk();

    ComputerBuilder buildPower();

    Computer create();
}

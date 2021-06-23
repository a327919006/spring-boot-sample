package com.cn.boot.sample.design.pattern.builder;

/**
 * @author Chen Nan
 */
public class HighComputerBuilder implements ComputerBuilder {
    private Computer computer = new Computer();

    @Override
    public ComputerBuilder buildCpu() {
        computer.setCpu("高配CPU");
        return this;
    }

    @Override
    public ComputerBuilder buildMemory() {
        computer.setMemory("高配内存");
        return this;
    }

    @Override
    public ComputerBuilder buildDisk() {
        computer.setDisk("高配磁盘");
        return this;
    }

    @Override
    public ComputerBuilder buildPower() {
        computer.setPower("高配电源");
        return this;
    }

    @Override
    public Computer create() {
        return computer;
    }
}

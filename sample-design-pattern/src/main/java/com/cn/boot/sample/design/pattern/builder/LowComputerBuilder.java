package com.cn.boot.sample.design.pattern.builder;

/**
 * @author Chen Nan
 */
public class LowComputerBuilder implements ComputerBuilder {
    private Computer computer = new Computer();

    @Override
    public ComputerBuilder buildCpu() {
        computer.setCpu("低配CPU");
        return this;
    }

    @Override
    public ComputerBuilder buildMemory() {
        computer.setMemory("低配内存");
        return this;
    }

    @Override
    public ComputerBuilder buildDisk() {
        computer.setDisk("低配磁盘");
        return this;
    }

    @Override
    public ComputerBuilder buildPower() {
        computer.setPower("低配电源");
        return this;
    }

    @Override
    public Computer create() {
        return computer;
    }
}

package com.cn.boot.sample.design.pattern.builder;

/**
 * @author Chen Nan
 */
public class LowComputerBuilder implements Builder {
    private Computer computer = new Computer();

    @Override
    public Builder buildCpu() {
        computer.setCpu("低配CPU");
        return this;
    }

    @Override
    public Builder buildMemory() {
        computer.setMemory("低配内存");
        return this;
    }

    @Override
    public Builder buildDisk() {
        computer.setDisk("低配磁盘");
        return this;
    }

    @Override
    public Builder buildPower() {
        computer.setPower("低配电源");
        return this;
    }

    @Override
    public Computer create() {
        return computer;
    }
}

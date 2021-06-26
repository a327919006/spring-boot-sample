package com.cn.boot.sample.design.pattern.composite;

/**
 * @author Chen Nan
 */
public abstract class AbstractRoot {
    private String name;

    public AbstractRoot(String name) {
        this.name = name;
    }

    public abstract void addFile(AbstractRoot root);

    public abstract void removeFile(AbstractRoot root);

    public abstract void display(int depth);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

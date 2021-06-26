package com.cn.boot.sample.design.pattern.composite;

/**
 * @author Chen Nan
 */
public class MyFile extends AbstractRoot {
    public MyFile(String name) {
        super(name);
    }

    @Override
    public void addFile(AbstractRoot root) {

    }

    @Override
    public void removeFile(AbstractRoot root) {

    }

    @Override
    public void display(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("-");
        }
        //打印横线和当前文件名
        System.out.println(sb.toString() + this.getName());
    }
}

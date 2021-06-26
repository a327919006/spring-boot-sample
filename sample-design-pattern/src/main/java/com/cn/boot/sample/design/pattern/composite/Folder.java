package com.cn.boot.sample.design.pattern.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Nan
 */
public class Folder extends AbstractRoot {

    /**
     * 子文件夹列表
     */
    private List<AbstractRoot> folders = new ArrayList<>();

    public Folder(String name) {
        super(name);
    }

    @Override
    public void addFile(AbstractRoot root) {
        folders.add(root);
    }

    @Override
    public void removeFile(AbstractRoot root) {
        folders.remove(root);
    }

    @Override
    public void display(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("-");
        }
        //打印横线和当前文件名
        System.out.println(sb.toString() + this.getName());

        for (AbstractRoot r : folders) {
            //每个下级，横线多2个
            r.display(depth + 2);
        }
    }

    public List<AbstractRoot> getFolders() {
        return folders;
    }

    public void setFolders(List<AbstractRoot> folders) {
        this.folders = folders;
    }
}

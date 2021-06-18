package com.cn.boot.sample.design.pattern.singleton;

/**
 * 单例模式-饿汉
 *
 * @author Chen Nan
 */
public class SingletonHungry {
    private static volatile SingletonHungry instance = new SingletonHungry();

    /**
     * 构造函数私有化
     */
    private SingletonHungry() {
    }

    public static SingletonHungry getInstance() {
        return instance;
    }

    public void business() {
        System.out.println("饿汉-处理业务");
    }
}

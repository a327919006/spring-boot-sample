package com.cn.boot.sample.design.pattern.singleton;

/**
 * 单例模式-懒汉
 *
 * @author Chen Nan
 */
public class SingletonLazy {
    private static volatile SingletonLazy instance;

    private SingletonLazy() {
    }

    /**
     * 1、double check lock：双重检查锁定，多线程下保持高性能
     * 2、volatile的作用：禁止指令重排序
     * <p>
     * 1、为什么不直接在方法上加synchronized
     * 出于性能考虑，多线程并发获取实例时性能不好
     */
    public static SingletonLazy getInstance() {
        if (instance == null) {
            synchronized (SingletonLazy.class) {
                if (instance == null) {
                    instance = new SingletonLazy();
                }
            }
        }
        return instance;
    }

    public void business() {
        System.out.println("处理业务");
    }
}

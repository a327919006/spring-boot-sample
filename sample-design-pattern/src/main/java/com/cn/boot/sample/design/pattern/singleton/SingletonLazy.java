package com.cn.boot.sample.design.pattern.singleton;

/**
 * 单例模式-懒汉
 *
 * @author Chen Nan
 */
public class SingletonLazy {
    private static volatile SingletonLazy instance;

    /**
     * 构造函数私有化
     */
    private SingletonLazy() {
    }

    /**
     * 1、double check lock：双重检查锁定，多线程下保持高性能
     * 2、volatile的作用：禁止指令重排序
     * <p>
     * 1、为什么不直接在方法上加synchronized
     * 出于性能考虑，多线程并发获取实例时性能不好
     *
     * 2、instance = new SingletonLazy(); 并不是原⼦性操作
     * 1）分配空间给对象
     * 2）初始化对象
     * 3）将对象赋值给引⽤instance
     *
     * 假如指令重排 变成1->3->2顺序，则多线程时会发生返回未初始化的对象
     * *
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

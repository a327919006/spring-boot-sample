package com.cn.boot.sample.business.util.proxy;

import java.lang.reflect.Method;

/**
 * @author ChenNan
 */
public class SimpleAspect implements Aspect {
    @Override
    public boolean before(Object target, Method method, Object[] args) {
        //继承此类后实现此方法
        return true;
    }

    @Override
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        //继承此类后实现此方法
        return true;
    }

    @Override
    public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
        //继承此类后实现此方法
        return true;
    }
}

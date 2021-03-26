package com.cn.boot.sample.business.util.proxy;

import net.sf.cglib.proxy.Enhancer;

/**
 * @author ChenNan
 */
public class ProxyUtil {

    public static <T> T proxy(T target, Class<? extends Aspect> aspectClass) throws IllegalAccessException, InstantiationException {
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new CglibInterceptor(target, aspectClass.newInstance()));
        return (T) enhancer.create();
    }
}

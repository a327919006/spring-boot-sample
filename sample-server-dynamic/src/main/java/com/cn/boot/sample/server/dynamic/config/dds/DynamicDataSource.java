package com.cn.boot.sample.server.dynamic.config.dds;

import java.lang.annotation.*;

/**
 * 动态数据源注解
 * @author Louis
 * @date Jun 17, 2019
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicDataSource {

}
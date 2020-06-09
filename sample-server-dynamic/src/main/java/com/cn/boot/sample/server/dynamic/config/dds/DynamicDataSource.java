package com.cn.boot.sample.server.dynamic.config.dds;

import java.lang.annotation.*;

/**
 * 动态数据源注解
 *
 * @author ChenNan
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicDataSource {

}
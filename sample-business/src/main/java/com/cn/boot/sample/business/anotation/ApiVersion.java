package com.cn.boot.sample.business.anotation;

import java.lang.annotation.*;

/**
 * @author Chen Nan
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ApiVersion {
    String value() default "";
}
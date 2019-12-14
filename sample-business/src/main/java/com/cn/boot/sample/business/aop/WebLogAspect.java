package com.cn.boot.sample.business.aop;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 统一日志处理器
 *
 * @author Chen Nan
 */
@Component
@Aspect
@Slf4j
public class WebLogAspect {

    @Pointcut("execution(public * com.cn.boot.sample.business.controller..*.*(..))" +
            "&& @annotation(io.swagger.annotations.ApiOperation)")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ApiOperation apiOperation = signature.getMethod().getDeclaredAnnotation(ApiOperation.class);
        if (apiOperation != null) {
            // 方法描述
            String methodDesc = apiOperation.value();
            if (StringUtils.isNotBlank(methodDesc)) {
                log.info("====【{}】req->{}", methodDesc, joinPoint.getArgs());
            }
        }
    }

    @After("webLog()")
    public void doAfter(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ApiOperation apiOperation = signature.getMethod().getDeclaredAnnotation(ApiOperation.class);
        if (apiOperation != null) {
            // 方法描述
            String methodDesc = apiOperation.value();
            if (StringUtils.isNotBlank(methodDesc)) {
                log.info("====【{}】完成 ====", methodDesc);
            }
        }
    }
}

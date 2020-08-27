package com.cn.boot.sample.logback.aop;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * 日志ID处理器
 *
 * @author Chen Nan
 */
@Component
@Aspect
@Slf4j
public class LogIdAspect {

    @Pointcut("execution(public * com.cn.boot.sample.logback.controller..*.*(..))" +
            "&& @annotation(io.swagger.annotations.ApiOperation)")
    public void logId() {
    }

    @Before("logId()")
    public void doBefore(JoinPoint joinPoint) {
        String id = IdUtil.simpleUUID();
        log.info("doBefore id={}", id);
        MDC.put("RequestId", id);
    }

    @After("logId()")
    public void doAfter(JoinPoint joinPoint) {

    }
}

package com.cn.boot.sample.business.util.proxy;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Method;

/**
 * @author ChenNan
 */
@Slf4j
public class OperateLogAspect extends SimpleAspect {

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        RequestIdContextHolder.setRequestId(traceId());
        return true;
    }

    @Override
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        log.info("traceId=" + RequestIdContextHolder.getRequestId());
        return true;
    }

    public static String traceId() {
        return RandomStringUtils.randomAlphanumeric(16) + '@' +
                Thread.currentThread().getId();
    }
}

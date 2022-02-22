package com.cn.boot.sample.business.aop;

import com.cn.boot.sample.api.enums.ErrorCode;
import com.cn.boot.sample.api.exceptions.BusinessException;
import com.cn.boot.sample.api.model.dto.ReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 切面-签名校验
 *
 * @author Chen Nan
 */
@Component
@Aspect
@Slf4j
public class CheckAspect {

    @Pointcut("execution(public * com.cn.boot.sample.business.controller.AopController.*(..))")
    public void check() {
    }

    @Before("check()")
    public void doBefore(JoinPoint joinPoint) {
        ReqDTO reqDTO = (ReqDTO) joinPoint.getArgs()[0];
        if (reqDTO.getCheckSig() == null || !reqDTO.getCheckSig()) {
            throw new BusinessException(ErrorCode.ERROR_SIG);
        }
    }
}

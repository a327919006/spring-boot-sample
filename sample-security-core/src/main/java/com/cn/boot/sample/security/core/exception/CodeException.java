package com.cn.boot.sample.security.core.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常
 *
 * @author Chen Nan
 */
public class CodeException extends AuthenticationException {

    public CodeException(String msg) {
        super(msg);
    }
}

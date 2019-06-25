package com.cn.boot.sample.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常
 *
 * @author Chen Nan
 */
public class CaptchaException extends AuthenticationException {

    public CaptchaException(String msg) {
        super(msg);
    }
}

package com.cn.boot.sample.security.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 未认证异常
 *
 * @author Chen Nan
 * @date 2019/3/2.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UnauthorizedException extends RuntimeException {
    private int code = 1;
    private String msg = "请先登录";

    public UnauthorizedException(String msg) {
        super(msg);
        this.msg = msg;
    }
}

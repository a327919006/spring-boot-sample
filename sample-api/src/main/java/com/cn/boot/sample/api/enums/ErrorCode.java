package com.cn.boot.sample.api.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS("请求成功", 0),
    SYSTEM_BUSY("系统繁忙，此时请求方稍后重试", -1),
    ERROR_SIG("签名错误", 4001),
    ERROR_TOKEN("Token错误", 4002),
    ERROR_PARAM_POST("POST参数不合法", 4003),
    ERROR_PARAM_BUSINESS("业务参数不合法", 4004),
    SYSTEM_ERROR("系统错误", 500),
    ;
    private String message;
    private Integer code;

    ErrorCode(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
}

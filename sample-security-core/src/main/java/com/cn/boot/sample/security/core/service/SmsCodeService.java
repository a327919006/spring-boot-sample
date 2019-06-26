package com.cn.boot.sample.security.core.service;

/**
 * 短信验证码服务接口
 *
 * @author Chen Nan
 */
public interface SmsCodeService {
    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @return 验证码
     */
    String sendSmsCode(String phone);
}

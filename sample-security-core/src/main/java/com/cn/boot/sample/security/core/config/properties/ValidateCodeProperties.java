package com.cn.boot.sample.security.core.config.properties;

import lombok.Data;

/**
 * 验证码配置
 *
 * @author Chen Nan
 */
@Data
public class ValidateCodeProperties {
    /**
     * 图形验证码
     */
    private ImageCodeProperties image = new ImageCodeProperties();

    /**
     * 短信验证码
     */
    private SmsCodeProperties sms = new SmsCodeProperties();
}

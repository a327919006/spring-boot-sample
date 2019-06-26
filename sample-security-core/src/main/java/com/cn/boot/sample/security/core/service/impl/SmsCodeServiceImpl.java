package com.cn.boot.sample.security.core.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import com.cn.boot.sample.security.core.service.SmsCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Chen Nan
 */
@Slf4j
public class SmsCodeServiceImpl implements SmsCodeService {
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public String sendSmsCode(String phone) {
        String code = RandomUtil.randomNumbers(securityProperties.getCode().getSms().getLength());
        log.info("【短信验证码】发送, phone->{}, code={}", phone, code);
        return code;
    }
}

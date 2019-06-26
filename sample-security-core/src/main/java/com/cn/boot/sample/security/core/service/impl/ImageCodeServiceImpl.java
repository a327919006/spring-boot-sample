package com.cn.boot.sample.security.core.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ICaptcha;
import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import com.cn.boot.sample.security.core.service.ImageCodeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Chen Nan
 */
public class ImageCodeServiceImpl implements ImageCodeService {
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ICaptcha createImageCode() {
        return CaptchaUtil.createCircleCaptcha(
                securityProperties.getCode().getImage().getWidth(),
                securityProperties.getCode().getImage().getHeight(),
                securityProperties.getCode().getImage().getLength(),
                5);
    }
}

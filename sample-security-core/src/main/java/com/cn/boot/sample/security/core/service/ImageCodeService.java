package com.cn.boot.sample.security.core.service;

import cn.hutool.captcha.ICaptcha;

/**
 * 图形验证码服务接口
 *
 * @author Chen Nan
 */
public interface ImageCodeService {
    /**
     * 创建图形验证码
     *
     * @return 图形验证码
     */
    ICaptcha createImageCode();
}

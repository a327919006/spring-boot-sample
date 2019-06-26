package com.cn.boot.sample.security.core.config;

import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import com.cn.boot.sample.security.core.service.ImageCodeService;
import com.cn.boot.sample.security.core.service.SmsCodeService;
import com.cn.boot.sample.security.core.service.impl.ImageCodeServiceImpl;
import com.cn.boot.sample.security.core.service.impl.SmsCodeServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    @Bean
    @ConditionalOnMissingBean(ImageCodeService.class)
    public ImageCodeService imageCodeService() {
        return new ImageCodeServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(SmsCodeService.class)
    public SmsCodeService smsCodeService() {
        return new SmsCodeServiceImpl();
    }
}

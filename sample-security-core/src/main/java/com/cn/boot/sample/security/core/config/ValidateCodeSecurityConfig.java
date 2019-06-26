package com.cn.boot.sample.security.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

/**
 * @author ChenNan
 */
@Component
public class ValidateCodeSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final Filter imageCodeFilter;
    private final Filter smsCodeFilter;

    @Autowired
    public ValidateCodeSecurityConfig(Filter imageCodeFilter, Filter smsCodeFilter) {
        this.imageCodeFilter = imageCodeFilter;
        this.smsCodeFilter = smsCodeFilter;
    }

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(imageCodeFilter, AbstractPreAuthenticatedProcessingFilter.class);
        http.addFilterBefore(smsCodeFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }

}

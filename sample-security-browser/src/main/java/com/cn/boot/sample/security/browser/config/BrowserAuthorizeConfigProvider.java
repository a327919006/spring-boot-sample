package com.cn.boot.sample.security.browser.config;

import com.cn.boot.sample.security.core.authorize.AuthorizeConfigProvider;
import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class BrowserAuthorizeConfigProvider implements AuthorizeConfigProvider {
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers(
                "/client/*"
        ).permitAll();
        return false;
    }
}

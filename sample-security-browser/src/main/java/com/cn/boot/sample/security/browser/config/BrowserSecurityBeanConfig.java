package com.cn.boot.sample.security.browser.config;

import com.cn.boot.sample.security.browser.logout.SampleLogoutSuccessHandler;
import com.cn.boot.sample.security.browser.session.SampleInvalidSessionStrategy;
import com.cn.boot.sample.security.browser.session.SampleSessionExpiredStrategy;
import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * @author Chen Nan
 */
@Configuration
public class BrowserSecurityBeanConfig {
    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    @ConditionalOnMissingBean
    public InvalidSessionStrategy invalidSessionStrategy() {
        return new SampleInvalidSessionStrategy(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new SampleSessionExpiredStrategy(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new SampleLogoutSuccessHandler(securityProperties);
    }
}

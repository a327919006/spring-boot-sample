package com.cn.boot.sample.security.browser.config;

import com.cn.boot.sample.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.cn.boot.sample.security.core.config.BaseWebSecurityConfig;
import com.cn.boot.sample.security.core.config.ValidateCodeSecurityConfig;
import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Chen Nan
 */
@Configuration
public class BrowserSecurityConfig extends BaseWebSecurityConfig {

    private final SecurityProperties securityProperties;
    private final UserDetailsService userDetailsService;
    private final ValidateCodeSecurityConfig validateCodeSecurityConfig;
    private final SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    public BrowserSecurityConfig(SecurityProperties securityProperties, @Qualifier("loginServiceImpl") UserDetailsService userDetailsService, ValidateCodeSecurityConfig validateCodeSecurityConfig, SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig) {
        this.securityProperties = securityProperties;
        this.userDetailsService = userDetailsService;
        this.validateCodeSecurityConfig = validateCodeSecurityConfig;
        this.smsCodeAuthenticationSecurityConfig = smsCodeAuthenticationSecurityConfig;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        applyPasswordAuthenticationConfig(http);
        http.apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .rememberMe()
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .and()
                .authorizeRequests()
                // 如果是/login.html直接放行，注意：谷歌浏览器自己会请求favicon.ico
                .antMatchers("/authentication/form",
                        "/authentication/require",
                        "/authentication/code/**",
                        "/favicon.ico").permitAll()
                .antMatchers(securityProperties.getBrowser().getLoginPage())
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();


    }
}

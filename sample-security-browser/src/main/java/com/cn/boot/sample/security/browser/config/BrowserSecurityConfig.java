package com.cn.boot.sample.security.browser.config;

import com.cn.boot.sample.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.cn.boot.sample.security.core.authorize.AuthorizeConfigManager;
import com.cn.boot.sample.security.core.config.BaseWebSecurityConfig;
import com.cn.boot.sample.security.core.config.ValidateCodeSecurityConfig;
import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * @author Chen Nan
 */
@Configuration
public class BrowserSecurityConfig extends BaseWebSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    @Qualifier("loginServiceImpl")
    private UserDetailsService userDetailsService;
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;
    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                securityProperties.getBrowser().getLoginPage(),
                "/authentication/code/**",
                "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        applyPasswordAuthenticationConfig(http);
        http.apply(validateCodeSecurityConfig)
                .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                // 设置记住我
                .rememberMe()
                // 设置记住我多久
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .and()
                .sessionManagement()
                // 设置session超时后跳转地址
                .invalidSessionStrategy(invalidSessionStrategy)
                // 设置同个账号运行同时登录多少个
                .maximumSessions(1)
                // true:只能登录x个，后面的登录失败. false:后面的踢掉之前登录的,默认false
//                .maxSessionsPreventsLogin(true)
                // 多账号登录处理方式
                .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .and()
                .and()
                .logout()
                .logoutUrl("/logout")
//                .logoutSuccessUrl("/logout.html")
                .logoutSuccessHandler(logoutSuccessHandler)
                // 退出登录后删除Cookie
//                .deleteCookies("JSESSIONID")
                .and()
                .csrf().disable();

        authorizeConfigManager.config(http.authorizeRequests());
    }
}

package com.cn.boot.sample.security.config;

import com.cn.boot.sample.security.core.config.SecurityProperties;
import com.cn.boot.sample.security.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Chen Nan
 */
@Configuration
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginService loginService;
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private MyAuthenticationSuccessHandler successHandler;
    @Autowired
    private MyAuthenticationFailureHandler failureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.httpBasic()
//                .and()
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated();


        http.cors()
                .and()
                .csrf().disable()
                .formLogin()
                // 设置登录页
                .loginPage("/authentication/require")
                // 指定登录接口地址
                .loginProcessingUrl("/authentication/form")
                // 指定认证成功处理器
                .successHandler(successHandler)
                // 指定认证失败处理器
                .failureHandler(failureHandler)
                .and()
                .authorizeRequests()
                // 如果是/login.html直接放行，注意：谷歌浏览器自己会请求favicon.ico
                .antMatchers("/login").permitAll()
                .antMatchers("/authentication/form").permitAll()
                .antMatchers("/authentication/require").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers(securityProperties.getBrowser().getLoginPage()).permitAll()
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

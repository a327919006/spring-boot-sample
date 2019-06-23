package com.cn.boot.sample.security.controller;

import com.cn.boot.sample.api.exceptions.UnauthorizedException;
import com.cn.boot.sample.security.core.config.SecurityProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/authentication")
@Api(tags = "安全认证", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SecurityController {
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    @ApiOperation("判断是否跳转登录页")
    @GetMapping("/require")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();
            log.info("redirectUrl = {}", redirectUrl);
            String htmlEnd = ".html";
            if (StringUtils.endsWithIgnoreCase(redirectUrl, htmlEnd)) {
                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
            }
        }
        throw new UnauthorizedException();
    }

    @ApiOperation("获取认证信息")
    @GetMapping("/get")
    public UserDetails get(@AuthenticationPrincipal @ApiIgnore UserDetails userDetails) throws IOException {
        // 方式一：SecurityContextHolder
//        return SecurityContextHolder.getContext().getAuthentication();
        // 方式二：@AuthenticationPrincipal
        return userDetails;
    }
}

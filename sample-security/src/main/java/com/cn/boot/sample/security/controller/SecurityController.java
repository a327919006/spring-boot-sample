package com.cn.boot.sample.security.controller;

import cn.hutool.captcha.ICaptcha;
import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import com.cn.boot.sample.security.core.exception.UnauthorizedException;
import com.cn.boot.sample.security.core.service.ImageCodeService;
import com.cn.boot.sample.security.core.service.SmsCodeService;
import com.cn.boot.sample.security.core.util.CodeValidateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private ImageCodeService imageCodeService;
    @Autowired
    private SmsCodeService smsCodeService;

    @ApiOperation("判断是否跳转登录页")
    @GetMapping("/require")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();
            log.info("redirectUrl = {}", redirectUrl);
            String htmlEnd = ".html";
            if (StringUtils.endsWithIgnoreCase(redirectUrl, htmlEnd)) {
                response.sendRedirect(securityProperties.getBrowser().getLoginPage());
                return;
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

    @ApiOperation("获取图片验证码")
    @GetMapping("/code/image")
    public void imageCode(HttpSession session, HttpServletResponse response) throws IOException {
        ICaptcha captcha = imageCodeService.createImageCode();

        // 禁止图像缓存
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");

        // 将图像输出到Servlet输出流中。
        ServletOutputStream outputStream = response.getOutputStream();
        captcha.write(outputStream);
        // 将验证码保存到Session中。
        session.setAttribute(CodeValidateUtil.SESSION_KEY, captcha.getCode());
        session.setAttribute(CodeValidateUtil.SESSION_TIME_KEY, System.currentTimeMillis());
        outputStream.flush();
        outputStream.close();
    }

    @ResponseBody
    @ApiOperation("获取短信验证码")
    @GetMapping("/code/sms/{phone}")
    public void msgCode(@PathVariable String phone, HttpSession session) {
        String code = smsCodeService.sendSmsCode(phone);
        // 将验证码保存到Session中。
        session.setAttribute(CodeValidateUtil.SESSION_KEY, code);
        session.setAttribute(CodeValidateUtil.SESSION_TIME_KEY, System.currentTimeMillis());
    }
}

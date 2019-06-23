package com.cn.boot.sample.security.config;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.security.core.config.SecurityProperties;
import com.cn.boot.sample.security.core.enums.LoginTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Chen Nan
 */
@Component
@Slf4j
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("【登录成功】");

        if (securityProperties.getBrowser().getLoginType().equals(LoginTypeEnum.JSON)) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().print(JSONUtil.toJsonStr(authentication));
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}

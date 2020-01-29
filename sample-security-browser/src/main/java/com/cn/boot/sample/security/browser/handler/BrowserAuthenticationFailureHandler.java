package com.cn.boot.sample.security.browser.handler;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import com.cn.boot.sample.security.core.enums.LoginTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Chen Nan
 */
@Slf4j
public class BrowserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private SecurityProperties securityProperties;

    public BrowserAuthenticationFailureHandler(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.info("【登录失败】");
        if (securityProperties.getBrowser().getLoginType().equals(LoginTypeEnum.JSON)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().print(JSONUtil.toJsonStr(new Error(exception.getMessage())));
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}

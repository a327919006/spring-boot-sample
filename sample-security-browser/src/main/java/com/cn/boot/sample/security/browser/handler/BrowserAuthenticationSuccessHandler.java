package com.cn.boot.sample.security.browser.handler;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import com.cn.boot.sample.security.core.enums.LoginTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Chen Nan
 */
@Slf4j
public class BrowserAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private SecurityProperties securityProperties;

    public BrowserAuthenticationSuccessHandler(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("【登录成功】");

        if (securityProperties.getBrowser().getLoginType().equals(LoginTypeEnum.JSON)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().print(JSONUtil.toJsonStr(authentication));
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}

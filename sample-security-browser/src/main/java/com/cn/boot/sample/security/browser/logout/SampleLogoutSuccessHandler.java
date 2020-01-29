package com.cn.boot.sample.security.browser.logout;

import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Chen Nan
 */
@Slf4j
public class SampleLogoutSuccessHandler implements LogoutSuccessHandler {
    private SecurityProperties securityProperties;

    public SampleLogoutSuccessHandler(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("退出登录成功");
        if (StringUtils.isNotBlank(securityProperties.getBrowser().getLogoutPage())) {
            response.sendRedirect(securityProperties.getBrowser().getLogoutPage());
        } else {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("退出登录成功");
        }
    }
}

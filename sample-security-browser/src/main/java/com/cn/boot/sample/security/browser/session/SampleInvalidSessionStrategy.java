package com.cn.boot.sample.security.browser.session;

import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Chen Nan
 */
@Slf4j
public class SampleInvalidSessionStrategy implements InvalidSessionStrategy {
    private SecurityProperties securityProperties;

    public SampleInvalidSessionStrategy(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();
        String htmlEnd = ".html";
        if (StringUtils.endsWithIgnoreCase(uri, htmlEnd)) {
            response.sendRedirect(securityProperties.getBrowser().getLoginPage());
        } else {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("会话过期");
        }
    }
}

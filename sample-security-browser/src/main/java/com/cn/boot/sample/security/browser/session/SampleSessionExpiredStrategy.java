package com.cn.boot.sample.security.browser.session;

import com.cn.boot.sample.security.core.config.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Chen Nan
 */
@Slf4j
public class SampleSessionExpiredStrategy implements SessionInformationExpiredStrategy {
    private SecurityProperties securityProperties;

    public SampleSessionExpiredStrategy(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        HttpServletRequest request = event.getRequest();
        HttpServletResponse response = event.getResponse();
        SessionInformation sessionInformation = event.getSessionInformation();

        String uri = request.getRequestURI();
        String htmlEnd = ".html";
        if (StringUtils.endsWithIgnoreCase(uri, htmlEnd)) {
            response.sendRedirect(securityProperties.getBrowser().getLoginPage());
        } else {
            log.info("sessionId = " + sessionInformation.getSessionId());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("您的账号于第三方登录");
        }
    }
}

package com.cn.boot.sample.business.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域过滤器
 *
 * @author Chen Nan
 */
@Slf4j
@Component
@WebFilter(filterName = "cors", urlPatterns = "/*")
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        // 允许请求的域名，*为所有，*可改成指定域名，http://test.com.cn,http://manager.com.cn
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 允许的请求方法
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE ,PUT");
        // 允许的有效时间
        response.setHeader("Access-Control-Max-Age", "3600");
        // 允许的请求头
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Authorization, accessToken, Content-Type, Origin, Accept, User-Agent");
        // 允许访问cookie
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if (RequestMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }
}

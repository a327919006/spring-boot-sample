package com.cn.boot.sample.business.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 跨域过滤器
 *
 * @author Chen Nan
 */
@Slf4j
@Component
@WebFilter(filterName = "requestWrapper", urlPatterns = "/*")
public class RequestWrapperFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (req instanceof HttpServletRequest) {
            requestWrapper = new SampleRequestWrapper((HttpServletRequest) req);
        }
        if (requestWrapper == null) {
            chain.doFilter(req, res);
        } else {
            chain.doFilter(requestWrapper, res);
        }
    }
}

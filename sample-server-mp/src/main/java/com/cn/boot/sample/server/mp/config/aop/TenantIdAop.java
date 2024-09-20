package com.cn.boot.sample.server.mp.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 租户ID切面，解析用户信息，获取租户ID，放入上下文
 *
 * @author Chen Nan
 */
@Slf4j
@Component
public class TenantIdAop implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从header中获取jwtToken，解析用户信息，获取租户ID，这里简化下，直接从头部获取租户ID
        String tenantId = request.getHeader("tenant_id");
        if (StringUtils.isNotEmpty(tenantId)) {
            TenantIdContextHolder.setTenantId(Long.parseLong(tenantId));
        }
        return true;
    }
}

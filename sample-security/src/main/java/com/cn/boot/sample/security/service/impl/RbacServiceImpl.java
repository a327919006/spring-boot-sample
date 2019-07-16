package com.cn.boot.sample.security.service.impl;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.security.service.RbacService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Chen Nan
 */
@Component
@Slf4j
public class RbacServiceImpl implements RbacService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        log.info("【hasPermission】");
        Object principal = authentication.getPrincipal();
        log.info("authentication={}", JSONUtil.toJsonStr(authentication));
        log.info("principal={}", JSONUtil.toJsonStr(principal));

        boolean hasPermission = false;

        String adminUser = "admin";
        if (principal instanceof User) {
            if (StringUtils.equals(((User) principal).getUsername(), adminUser)) {
                hasPermission = true;
            }
        } else if (principal instanceof String) {
            if (principal.equals(adminUser)) {
                hasPermission = true;
            }
        }
        return hasPermission;
    }
}

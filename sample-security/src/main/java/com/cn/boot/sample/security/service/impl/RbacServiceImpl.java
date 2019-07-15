package com.cn.boot.sample.security.service.impl;

import com.cn.boot.sample.security.service.RbacService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Chen Nan
 */
@Service
@Slf4j
public class RbacServiceImpl implements RbacService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        log.info("【hasPermission】");
        Object principal = authentication.getPrincipal();

        boolean hasPermission = false;

        if (principal instanceof User) {
            if (StringUtils.equals(((User) principal).getUsername(), "admin")) {
                hasPermission = true;
            }
        }
        return hasPermission;
    }
}

package com.cn.boot.sample.security.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Chen Nan
 */
public interface RbacService {

    boolean hasPermission(HttpServletRequest request, Authentication authentication);

}

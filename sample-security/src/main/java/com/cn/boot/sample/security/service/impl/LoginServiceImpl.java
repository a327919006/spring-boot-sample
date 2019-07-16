package com.cn.boot.sample.security.service.impl;

import com.cn.boot.sample.api.enums.UserStatusEnum;
import com.cn.boot.sample.api.model.po.User;
import com.cn.boot.sample.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Component
@Slf4j
public class LoginServiceImpl implements UserDetailsService {
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.get(new User().setUsername(username));
        if (user != null) {
            Integer status = user.getStatus();
            boolean normal = status.equals(UserStatusEnum.NORMAL.getValue());
            return new org.springframework.security.core.userdetails.User(
                    username,
                    user.getPassword(),
                    normal,
                    true,
                    true,
                    true,
                    AuthorityUtils.createAuthorityList("admin"));
        }
        return null;
    }
}

package com.cn.boot.sample.server.service;

import com.cn.boot.sample.api.model.po.User;
import com.cn.boot.sample.api.service.IUserService;
import com.cn.boot.sample.dal.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Chen Nan
 */
@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User, Long>
        implements IUserService, UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 应该去数据库获取用户信息
        return new org.springframework.security.core.userdetails.User(username, "123456", AuthorityUtils.createAuthorityList("admin"));
    }
}

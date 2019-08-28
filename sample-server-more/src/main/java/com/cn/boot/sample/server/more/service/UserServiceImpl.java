package com.cn.boot.sample.server.more.service;

import com.cn.boot.sample.api.model.po.User;
import com.cn.boot.sample.api.service.UserService;
import com.cn.boot.sample.dal.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author Chen Nan
 */
@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User, Long>
        implements UserService {
}

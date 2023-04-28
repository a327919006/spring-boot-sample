package com.cn.boot.sample.doris.service.impl;

import com.cn.boot.sample.doris.mapper.UserMapper;
import com.cn.boot.sample.doris.model.User;
import com.cn.boot.sample.doris.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chen Nan
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Override
    public int insert(List<User> user) {
        return mapper.insert(user);
    }

}

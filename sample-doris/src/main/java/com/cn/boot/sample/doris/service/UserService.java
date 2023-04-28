package com.cn.boot.sample.doris.service;


import com.cn.boot.sample.doris.model.User;

import java.util.List;

/**
 * @author Chen Nan
 */
public interface UserService {
    int insert(List<User> user);

}

package com.cn.boot.sample.server.mp.service;

import com.cn.boot.sample.api.model.po.User;
import com.cn.boot.sample.api.service.UserService;
import com.cn.boot.sample.dal.mp.mapper.UserMapper;
import com.github.pagehelper.Page;
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
    private UserMapper userMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userMapper.deleteById(id);
    }

    @Override
    public int delete(User record) {
        return userMapper.deleteById(record);
    }

    @Override
    public int insert(User record) {
        return userMapper.insert(record);
    }

    @Override
    public int insertSelective(User record) {
        return userMapper.insert(record);
    }

    @Override
    public User selectByPrimaryKey(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        return userMapper.updateById(record);
    }

    @Override
    public int updateByPrimaryKey(User record) {
        return userMapper.updateById(record);
    }

    @Override
    public int count(User record) {
        return 0;
    }

    @Override
    public User get(User record) {
        return null;
    }

    @Override
    public List<User> list(User record) {
        return null;
    }

    @Override
    public List<User> listByCondition(Object record) {
        return null;
    }

    @Override
    public Page<User> listPage(Object record) {
        return null;
    }
}

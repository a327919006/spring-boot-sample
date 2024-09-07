package com.cn.boot.sample.server.mp.service;

import com.cn.boot.sample.dal.mp.entity.People;
import com.cn.boot.sample.dal.mp.mapper.PeopleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Chen Nan
 */
@Service
@Slf4j
public class PeopleServiceImpl {
    @Autowired
    private PeopleMapper peopleMapper;

    public int deleteByPrimaryKey(Long id) {
        return peopleMapper.deleteById(id);
    }

    public int delete(People record) {
        return peopleMapper.deleteById(record);
    }

    public int insert(People record) {
        return peopleMapper.insert(record);
    }

    public int insertSelective(People record) {
        return peopleMapper.insert(record);
    }

    public People selectByPrimaryKey(Long id) {
        return peopleMapper.selectById(id);
    }

    public int updateByPrimaryKeySelective(People record) {
        return peopleMapper.updateById(record);
    }

    public int updateByPrimaryKey(People record) {
        return peopleMapper.updateById(record);
    }
}

package com.cn.boot.sample.server.jpa.more.service;

import com.cn.boot.sample.api.model.po.Teacher;
import com.cn.boot.sample.api.service.TeacherService;
import com.cn.boot.sample.server.jpa.more.dao.two.TeacherRepository;
import com.github.pagehelper.Page;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Chen Nan
 */
@Service
public class TeacherSericeImpl implements TeacherService {

    @Autowired
    private TeacherRepository repository;

    @Override
    public int deleteByPrimaryKey(String id) {
        repository.deleteById(id);
        return 1;
    }

    @Override
    public int delete(Teacher record) {
        repository.delete(record);
        return 1;
    }

    @Override
    public int insert(Teacher record) {
        repository.save(record);
        return 1;
    }

    @Override
    public int insertSelective(Teacher record) {
        repository.save(record);
        return 1;
    }

    @Override
    public Teacher selectByPrimaryKey(String id) {
        return repository.findById(id).get();
    }

    @Override
    public int updateByPrimaryKeySelective(Teacher record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(Teacher record) {
        return 0;
    }

    @Override
    public int count(Teacher record) {
        return 0;
    }

    @Override
    public Teacher get(Teacher record) {
        return null;
    }

    @Override
    public List<Teacher> list(Teacher record) {
        return null;
    }

    @Override
    public List<Teacher> listByCondition(Object record) {
        return null;
    }

    @Override
    public Page<Teacher> listPage(Object record) {
        return null;
    }
}

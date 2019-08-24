package com.cn.boot.sample.server.jpa.service;

import cn.hutool.core.util.IdUtil;
import com.cn.boot.sample.api.model.dto.student.StudentAddReq;
import com.cn.boot.sample.api.model.po.Student;
import com.cn.boot.sample.api.service.StudentService;
import com.cn.boot.sample.server.jpa.repository.StudentRepository;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * @author Chen Nan
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository repository;

    @Override
    public Student insert(StudentAddReq req) {
        Student student = new Student();
        BeanUtils.copyProperties(req, student);
        student.setId(IdUtil.simpleUUID());
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        return repository.save(student);
    }

    @Override
    public Student findFirstByNameAndAge(String name, Integer age) {
        return repository.findFirstByNameAndAge(name, age);
    }
}

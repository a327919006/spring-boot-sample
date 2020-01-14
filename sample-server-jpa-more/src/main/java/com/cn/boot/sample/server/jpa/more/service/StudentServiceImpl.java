package com.cn.boot.sample.server.jpa.more.service;

import cn.hutool.core.util.IdUtil;
import com.cn.boot.sample.api.model.dto.DataGrid;
import com.cn.boot.sample.api.model.dto.student.StudentAddReq;
import com.cn.boot.sample.api.model.po.Student;
import com.cn.boot.sample.api.model.vo.student.StudentRsp;
import com.cn.boot.sample.api.service.StudentService;
import com.cn.boot.sample.server.jpa.more.dao.one.StudentRepository;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public DataGrid listPage(String name, Integer age) {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Student> list = repository.findByNameAndAgeOrderByCreateTime(name, age, pageable);
        DataGrid dataGrid = new DataGrid();
        dataGrid.setRows(list.getContent());
        dataGrid.setTotal(list.getTotalElements());
        return dataGrid;
    }

    @Override
    public List<Student> list(String name, Integer age) {
        Pageable pageable = PageRequest.of(1, 10);
        return repository.findByNameAndAge(name, age, pageable);
    }

    @Override
    public List<Student> findIdAndName(String name, Integer age) {
        Pageable pageable = PageRequest.of(1, 10);
        return repository.findIdAndName(name, age, pageable);
    }

    @Override
    public DataGrid findIdAndNamePage(String name, Integer age) {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Student> list = repository.findIdAndNamePage(name, age, pageable);
        DataGrid dataGrid = new DataGrid();
        dataGrid.setRows(list.getContent());
        dataGrid.setTotal(list.getTotalElements());
        return dataGrid;
    }

    @Override
    public List<Student> findByName(String name) {
        return null;
    }

    @Override
    public int updateAgeByName(Integer age, String name) {
        return 0;
    }

    @Override
    public int upsert(Student student) {
        return 0;
    }

    @Override
    public int insertInfo(StudentAddReq req) {
        return 0;
    }

    @Override
    public List<Student> findByIdList(List<String> idList, int age) {
        return null;
    }

    @Override
    public List<StudentRsp> findNameByAge(int age) {
        return null;
    }
}

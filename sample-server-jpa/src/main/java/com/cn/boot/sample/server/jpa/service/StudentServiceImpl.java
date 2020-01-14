package com.cn.boot.sample.server.jpa.service;

import cn.hutool.core.util.IdUtil;
import com.cn.boot.sample.api.model.dto.DataGrid;
import com.cn.boot.sample.api.model.dto.student.StudentAddReq;
import com.cn.boot.sample.api.model.po.Student;
import com.cn.boot.sample.api.model.vo.student.StudentRsp;
import com.cn.boot.sample.api.service.StudentService;
import com.cn.boot.sample.server.jpa.dao.StudentDao;
import com.cn.boot.sample.server.jpa.repository.StudentRepository;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Chen Nan
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository repository;
    @Autowired
    private StudentDao dao;

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
        return dao.find(name);
    }

    /**
     * 根据姓名更新年龄
     *
     * @param age  年龄
     * @param name 姓名
     * @return 更新条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAgeByName(Integer age, String name) {
        return repository.updateAgeByName(age, new Date(), name);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int upsert(Student student) {
        return repository.upsert(student.getId(), student.getName(), student.getAge(), student.getCreateTime(), student.getUpdateTime());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertInfo(StudentAddReq req) {
        return repository.insertInfo(IdUtil.simpleUUID(), req);
    }

    @Override
    public List<Student> findByIdList(List<String> idList, int age) {
        return repository.findByIdList(idList, age);
    }

    @Override
    public List<StudentRsp> findNameByAge(int age) {
        return repository.findNameByAge(age);
    }
}

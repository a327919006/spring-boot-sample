package com.cn.boot.sample.server.jpa.repository;

import com.cn.boot.sample.api.model.po.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Chen Nan
 */
public interface StudentRepository extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {
    /**
     * 根据条件查询
     *
     * @param name 姓名
     * @param age  年龄
     * @return 学生信息
     */
    Student findFirstByNameAndAge(String name, Integer age);
}
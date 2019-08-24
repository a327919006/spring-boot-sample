package com.cn.boot.sample.api.service;

import com.cn.boot.sample.api.model.dto.student.StudentAddReq;
import com.cn.boot.sample.api.model.po.Student;

/**
 * @author Chen Nan
 */
public interface StudentService {
    /**
     * 添加学生
     * @param req 学生信息
     * @return 学生信息
     */
    Student insert(StudentAddReq req);

    /**
     * 根据条件查询
     *
     * @param name 姓名
     * @param age  年龄
     * @return 学生信息
     */
    Student findFirstByNameAndAge(String name, Integer age);
}

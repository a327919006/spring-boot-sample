package com.cn.boot.sample.api.service;

import com.cn.boot.sample.api.model.dto.DataGrid;
import com.cn.boot.sample.api.model.dto.student.StudentAddReq;
import com.cn.boot.sample.api.model.po.Student;

import java.util.List;

/**
 * @author Chen Nan
 */
public interface StudentService {
    /**
     * 添加学生
     *
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

    /**
     * 分页查询
     *
     * @param name 姓名
     * @param age  年龄
     * @return 学生列表、总数
     */
    DataGrid listPage(String name, Integer age);

    /**
     * 分页查询
     *
     * @param name 姓名
     * @param age  年龄
     * @return 学生列表
     */
    List<Student> list(String name, Integer age);

    /**
     * 获取id，姓名列表
     *
     * @param name 姓名
     * @param age  年龄
     * @return 学生列表
     */
    List<Student> findIdAndName(String name, Integer age);

    /**
     * 获取id，姓名列表
     *
     * @param name 姓名
     * @param age  年龄
     * @return 学生列表
     */
    DataGrid findIdAndNamePage(String name, Integer age);

}

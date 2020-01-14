package com.cn.boot.sample.api.service;

import com.cn.boot.sample.api.model.dto.DataGrid;
import com.cn.boot.sample.api.model.dto.student.StudentAddReq;
import com.cn.boot.sample.api.model.po.Student;
import com.cn.boot.sample.api.model.vo.student.StudentRsp;

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

    /**
     * 根据姓名获取列表
     *
     * @param name 姓名
     * @return 学生列表
     */
    List<Student> findByName(String name);

    /**
     * 根据姓名更新年龄
     *
     * @param age  年龄
     * @param name 姓名
     * @return 更新条数
     */
    int updateAgeByName(Integer age, String name);

    /**
     * 插入/更新
     *
     * @param student 学生数据
     * @return 操作结果
     */
    int upsert(Student student);

    /**
     * 插入
     *
     * @param req 学生数据
     * @return 操作结果
     */
    int insertInfo(StudentAddReq req);

    /**
     * 查询，使用IN List
     *
     * @param idList ID列表
     * @param age    年龄
     * @return 操作结果
     */
    List<Student> findByIdList(List<String> idList, int age);

    /**
     * 查询，返回自定义对象
     *
     * @param age 年龄
     * @return 学生列表
     */
    List<StudentRsp> findNameByAge(int age);
}

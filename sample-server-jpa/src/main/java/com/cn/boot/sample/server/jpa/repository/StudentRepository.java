package com.cn.boot.sample.server.jpa.repository;

import com.cn.boot.sample.api.model.po.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 官方文档： https://docs.spring.io/spring-data/jpa/docs/2.1.10.RELEASE/reference/html/
 *
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

    /**
     * 分页查询
     *
     * @param name     姓名
     * @param age      年龄
     * @param pageable 分页参数
     * @return 学生列表
     */
    Page<Student> findByNameAndAgeOrderByCreateTime(String name, Integer age, Pageable pageable);

    /**
     * 分页查询
     *
     * @param name     姓名
     * @param age      年龄
     * @param pageable 分页参数
     * @return 学生列表
     */
    List<Student> findByNameAndAge(String name, Integer age, Pageable pageable);

    /**
     * 获取id，姓名列表
     *
     * @param name     姓名
     * @param age      年龄
     * @param pageable 分页参数
     * @return 学生列表
     */
    @Query(value = "select * from student where name=?1 and age=?2 order by create_time desc", nativeQuery = true)
    List<Student> findIdAndName(String name, Integer age, Pageable pageable);

    /**
     * 获取id，姓名列表
     *
     * @param name     姓名
     * @param age      年龄
     * @param pageable 分页参数
     * @return 学生列表
     */
    @Query(value = "select * from student where name=?1 and age=?2 order by create_time desc",
            countQuery = "select count(1) from student where name=?1 and age=?2",
            nativeQuery = true)
    Page<Student> findIdAndNamePage(String name, Integer age, Pageable pageable);

    /**
     * 根据姓名更新年龄
     *
     * @param age  年龄
     * @param name 姓名
     * @return 更新条数
     */
    @Modifying
    @Query("update Student set age=?1 where name = ?2")
    int updateAgeByName(Integer age, String name);
}
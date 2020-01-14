package com.cn.boot.sample.server.jpa.repository;

import com.cn.boot.sample.api.model.dto.student.StudentAddReq;
import com.cn.boot.sample.api.model.po.Student;
import com.cn.boot.sample.api.model.vo.student.StudentRsp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
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
     * @param age        年龄
     * @param updateTime 更新时间
     * @param name       姓名
     * @return 更新条数
     */
    @Modifying
    @Query("update Student set age=?1, update_time=?2 where name = ?3")
    int updateAgeByName(Integer age, Date updateTime, String name);

    /**
     * 插入/更新，使用on duplicate key update
     *
     * @param id         id
     * @param name       姓名
     * @param age        年龄
     * @param createTime 创建时间
     * @param updateTime 更新时间
     * @return 操作结果
     */
    @Modifying
    @Query(value = "INSERT INTO `boot_sample`.`student`(`id`, `name`, `age`, `create_time`, `update_time`) " +
            "VALUES (?1, ?2, ?3, ?4, ?5) " +
            "on duplicate key update `age` = ?3, `update_time`= ?5", nativeQuery = true)
    int upsert(String id, String name, Integer age, LocalDateTime createTime, LocalDateTime updateTime);

    /**
     * 插入，使用自定义对象StudentAddReq
     *
     * @param id  id
     * @param req 学生信息
     * @return 操作结果
     */
    @Modifying
    @Query(value = "INSERT INTO `boot_sample`.`student`(`id`, `name`, `age`) " +
            "VALUES (:id, :#{#req.name}, :#{#req.age}) ", nativeQuery = true)
    int insertInfo(@Param("id") String id, @Param("req") StudentAddReq req);

    /**
     * 查询，使用IN List
     *
     * @param idList ID列表
     * @param age    年龄
     * @return 操作结果
     */
    @Query(value = "SELECT * FROM `boot_sample`.`student` " +
            "WHERE id IN(:idList) and age = :age", nativeQuery = true)
    List<Student> findByIdList(@Param("idList") List<String> idList, @Param("age") int age);

    /**
     * 查询，返回自定义对象
     *
     * @param age    年龄
     * @return 学生列表
     */
    @Query("SELECT new com.cn.boot.sample.api.model.vo.student.StudentRsp(name, createTime) FROM Student WHERE age = ?1")
    List<StudentRsp> findNameByAge(Integer age);
}
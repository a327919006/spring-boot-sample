package com.cn.boot.sample.server.jpa.more.dao.two;

import com.cn.boot.sample.api.model.po.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Chen Nan
 */
public interface TeacherRepository extends JpaRepository<Teacher, String>, JpaSpecificationExecutor<Teacher> {
}
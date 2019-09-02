package com.cn.boot.sample.server.jpa.dao;

import com.cn.boot.sample.api.model.po.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Chen Nan
 */
@Repository
public class StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Student> find(String name) {
        String sql = "SELECT * FROM student WHERE name = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Student.class), name);
    }
}

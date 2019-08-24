package com.cn.boot.sample.api.model.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "student")
@Data
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", insertable = false, nullable = false)
    private String id;

    /**
     * 姓名
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 年龄
     */
    @Column(name = "age", nullable = false)
    private Integer age;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;


}
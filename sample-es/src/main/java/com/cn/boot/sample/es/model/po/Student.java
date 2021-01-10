package com.cn.boot.sample.es.model.po;

import com.cn.boot.sample.es.util.DateTimeDeserializer;
import com.cn.boot.sample.es.util.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Student implements Serializable {

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
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime createTime;
}
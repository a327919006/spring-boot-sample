package com.cn.boot.sample.webflux.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Chen Nan
 */
@Data
@Document
public class Student {
    @Id
    private String id;
    private String name;
    private Integer age;
}

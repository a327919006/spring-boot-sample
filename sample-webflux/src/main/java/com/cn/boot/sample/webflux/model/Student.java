package com.cn.boot.sample.webflux.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * @author Chen Nan
 */
@Data
@Document
public class Student {
    @Id
    private String id;
    @NotNull
    private String name;
    @NotNull
    private Integer age;
}

package com.cn.boot.sample.es.model.dto;

import com.cn.boot.sample.es.util.DateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TeacherReq implements Serializable {

    private String id;
    private String name;
    private Integer age;

    private Integer page = 1;
    private Integer size = 10;

    @JsonDeserialize(using = DateDeserializer.class)
    private Date startTime;
    @JsonDeserialize(using = DateDeserializer.class)
    private Date endTime;


}
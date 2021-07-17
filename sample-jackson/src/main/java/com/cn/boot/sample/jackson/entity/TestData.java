package com.cn.boot.sample.jackson.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Chen Nan
 */
@Data
public class TestData implements Serializable {
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private Date updateTime;

    @JsonIgnore
    private String hide;

    @JsonProperty(value = "num")
    private Integer number;

    private String nullStr;
    private Boolean nullBoolean;
    private List<String> nullList;
    private String[] nullArray;
    private Object nullObj;
}

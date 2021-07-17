package com.cn.boot.sample.jackson.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Chen Nan
 */
@Data
public class TestData implements Serializable {
    private Long id;
    private String name;
    private LocalDateTime createTime;
    private Date updateTime;
}

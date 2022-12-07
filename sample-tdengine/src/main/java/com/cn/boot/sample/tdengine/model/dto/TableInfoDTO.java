package com.cn.boot.sample.tdengine.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Chen Nan
 */
@Data
public class TableInfoDTO {
    private String name;
    private Date createdTime;
    private Integer columns;
    private Integer tags;
    private Integer tables;
}

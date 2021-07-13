package com.cn.boot.sample.graphql.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Chen Nan
 */

@Data
public class BaseEntity implements Serializable {

    /**
     * ID
     */
    protected Long id;

    /**
     * 创建时间戳 (单位:秒)
     */
    protected Date createdTime;

    /**
     * 更新时间戳 (单位:秒)
     */
    protected Date updatedTime;


    public BaseEntity() {
        createdTime = new Date();
        updatedTime = createdTime;
    }

    private void doPreUpdate() {
        updatedTime = new Date();
    }
}
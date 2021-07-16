package com.cn.boot.sample.graphql.entity;

import cn.hutool.core.date.DateUtil;
import graphql.schema.DataFetchingEnvironment;
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
    protected Date createTime;

    /**
     * 更新时间戳 (单位:秒)
     */
    protected Date updateTime;

    public BaseEntity() {
        createTime = new Date();
        updateTime = createTime;
    }

    public String getCreateTime(DataFetchingEnvironment evn) {
        return DateUtil.formatDateTime(createTime);
    }

    private void doPreUpdate() {
        updateTime = new Date();
    }
}
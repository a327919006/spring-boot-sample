package com.cn.boot.sample.api.model.po;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Table(name = "sensitiveword")
public class SensitiveWord implements Serializable {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 敏感词
     */
    private String word;

    /**
     * 应用id
     */
    private String appId;
}
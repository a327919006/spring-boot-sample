package com.cn.boot.sample.es.model.po;

import lombok.Data;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.FieldType;

import java.io.Serializable;
import java.util.Date;

@Data
@IndexName
public class Teacher implements Serializable {

    private String id;

    /**
     * 姓名
     */
    @IndexField(fieldType = FieldType.KEYWORD)
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 创建时间
     */
    private Date createTime;
}
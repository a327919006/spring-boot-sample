package com.cn.boot.sample.es.model.po;

import com.cn.boot.sample.es.util.DateTimeDeserializer;
import com.cn.boot.sample.es.util.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.FieldType;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@IndexName
public class Student implements Serializable {

    @Id
    @Column(name = "id", insertable = false, nullable = false)
    private String id;

    /**
     * 姓名
     */
    @Column(name = "name", nullable = false)
    @IndexField(fieldType = FieldType.KEYWORD)
    private String name;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 年龄
     */
    @Column(name = "age", nullable = false)
    private Integer age;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime createTime;
}
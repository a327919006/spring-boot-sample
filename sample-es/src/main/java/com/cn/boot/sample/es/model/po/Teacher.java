package com.cn.boot.sample.es.model.po;

import com.cn.boot.sample.es.util.DateDeserializer;
import com.cn.boot.sample.es.util.DateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.dromara.easyes.annotation.*;
import org.dromara.easyes.annotation.rely.FieldType;
import org.dromara.easyes.annotation.rely.IdType;

import java.io.Serializable;
import java.util.Date;

@Data
@IndexName
@Settings(shardsNum = 1, replicasNum = 0, maxResultWindow = 10000)
public class Teacher implements Serializable {

    /**
     * 当字段命名为id且类型为String时，且不需要采用UUID及自定义ID类型时，可省略此注解
     * Id的生成类型支持以下几种:
     * IdType.NONE: 由ES自动生成,是默认缺省时的配置,无需您额外配置 推荐
     * IdType.UUID: 系统生成UUID,然后插入ES (不推荐)
     * IdType.CUSTOMIZE: 由用户自定义,用户自己对id值进行set,如果用户指定的id在es中不存在,则在insert时就会新增一条记录,如果用户指定的id在es中已存在记录,则自动更新该id对应的记录.
     */
    @IndexId(type = IdType.NONE)
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
    @IndexField(fieldType = FieldType.DATE, dateFormat = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date createTime;

    /**
     * ES匹配查询得分
     */
    @Score
    private Float score;

    /**
     * 场景一:标记es中不存在的字段
     * --@IndexField(exist = false)
     * 场景二:更新时,此字段非空字符串才会被更新
     * --@IndexField(strategy = FieldStrategy.NOT_EMPTY)
     * 场景三: 指定fieldData,需要对类型为text或keyword_tex字段聚合时,可指定其fieldData=true,否则es会报错.
     * --@IndexField(fieldType = FieldType.TEXT, fieldData = true)
     * 场景四:自定义字段名,比如该字段在es中叫wu-la,但在实体model中叫ula
     * --@IndexField("wu-la")
     * 场景五:支持日期字段在es索引中的format类型
     * --@IndexField(fieldType = FieldType.DATE, dateFormat = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
     * 场景六:支持指定字段在es索引中的分词器类型
     * --@IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_SMART, searchAnalyzer = Analyzer.IK_MAX_WORD)
     * 场景七：支持指定字段在es的索引中忽略大小写,以便在term查询时不区分大小写,仅对keyword类型字段生效,es的规则,并非框架限制.
     * --@IndexField(fieldType = FieldType.KEYWORD, ignoreCase = true)
     * 场景八:支持稠密向量类型 稠密向量类型,dims必须同时指定,非负 最大为2048
     * --@IndexField(fieldType = FieldType.DENSE_VECTOR, dims = 3)
     */
    @IndexField(exist = false)
    private String notExistsField;


}
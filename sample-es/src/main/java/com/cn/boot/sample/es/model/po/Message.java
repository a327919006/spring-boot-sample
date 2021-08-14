package com.cn.boot.sample.es.model.po;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

/**
 * @author Chen Nan
 */
@Data
@Accessors(chain = true)
//@Document(indexName = "message", type = "_doc", shards = 1, replicas = 0)
//@Document(indexName = "message", shards = 1, replicas = 0)
@Setting(shards = 1, replicas = 0)
public class Message {

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String user;

    /**
     * 可指定使用哪个分词器，如ik分词器
     */
//    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Integer)
    private Integer type;

    public static String getIndexName() {
        return "message";
    }
}

package com.cn.boot.sample.es.model.po;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author Chen Nan
 */
@Data
@Accessors(chain = true)
//@Document(indexName = "message", type = "_doc", shards = 1, replicas = 0)
@Document(indexName = "message", shards = 1, replicas = 0)
public class Message {

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String user;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;

    @Field(type = FieldType.Integer)
    private Integer type;

    public static String getIndexName() {
        return "message";
    }
}

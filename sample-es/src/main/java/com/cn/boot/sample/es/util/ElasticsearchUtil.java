package com.cn.boot.sample.es.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class ElasticsearchUtil {
    private RestHighLevelClient client;

    @PostConstruct
    public void init() {
        HttpHost httpHost = new HttpHost("localhost", 9200, "http");
        RestClientBuilder builder = RestClient.builder(httpHost);
        client = new RestHighLevelClient(builder);
    }

    public void createIndex() {
        CreateIndexRequest request = new CreateIndexRequest("student");
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
        );

        Map<String, Object> mapping = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> name = new HashMap<>();
        Map<String, Object> age = new HashMap<>();
        Map<String, Object> createTime = new HashMap<>();
        name.put("type", "text");
        age.put("type", "integer");
        createTime.put("type", "date");
        properties.put("name", name);
        properties.put("age", age);
        properties.put("age", age);

        mapping.put("properties", properties);
        request.mapping(mapping);
    }

    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            log.error("close error:", e);
        }
    }
}

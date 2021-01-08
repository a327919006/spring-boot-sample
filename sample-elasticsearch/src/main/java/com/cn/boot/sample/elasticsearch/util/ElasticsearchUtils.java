package com.cn.boot.sample.elasticsearch.util;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ChenNan
 */
@Component
public class ElasticsearchUtils {

    private RestHighLevelClient client;

    @PostConstruct
    public void init() {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")
                )
        );
    }

    public void insert() {

    }
}

package com.cn.boot.sample.es.util;

import com.cn.boot.sample.es.model.dto.StudentAddReq;
import com.cn.boot.sample.es.model.po.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.GetSourceRequest;
import org.elasticsearch.client.core.GetSourceResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class ElasticsearchUtil {
    private RestHighLevelClient client;

    /**
     * 初始化RestHighLevelClient
     */
    @PostConstruct
    public void init() {
        HttpHost httpHost = new HttpHost("localhost", 9200, "http");
        RestClientBuilder builder = RestClient.builder(httpHost);
        client = new RestHighLevelClient(builder);
    }

    /**
     * 创建索引
     *
     * @return 操作结果
     */
    public boolean createIndex(String index) {
        try {
            CreateIndexRequest request = new CreateIndexRequest(index);
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
            properties.put("createTime", createTime);

            mapping.put("properties", properties);
            request.mapping(mapping);

            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (Exception e) {
            log.error("createIndex error:", e);
        }
        return false;
    }

    /**
     * 删除索引
     *
     * @return 操作结果
     */
    public boolean deleteIndex(String index) {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(index);
            AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (Exception e) {
            log.error("deleteIndex error:", e);
        }
        return false;
    }

    public boolean save(String index, StudentAddReq req) {
        try {
            IndexRequest request = new IndexRequest(index);
            request.id(req.getId())
                    .source(JsonUtil.toJson(req), XContentType.JSON);

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            if (response.getResult() == DocWriteResponse.Result.CREATED
                    || response.getResult() == DocWriteResponse.Result.UPDATED) {
                return true;
            }
        } catch (Exception e) {
            log.error("save error:", e);
        }
        return false;
    }

    public Student getById(String index, String id) {
        try {
            GetSourceRequest request = new GetSourceRequest(index, id);
            GetSourceResponse response = client.getSource(request, RequestOptions.DEFAULT);
            String json = JsonUtil.toJson(response.getSource());
            return JsonUtil.fromJson(json, Student.class);
        } catch (Exception e) {
            log.error("getById error:", e);
        }
        return null;
    }

    /**
     * 获取所有
     */
    public List<Student> findAll(String index, int page, int size) {
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            // 分页
            searchSourceBuilder.from((page - 1) * size);
            searchSourceBuilder.size(size);
            // 按score排序（默认）
//            searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
            searchSourceBuilder.sort(new FieldSortBuilder("age").order(SortOrder.DESC));

            SearchRequest request = new SearchRequest(index);
            request.source(searchSourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            SearchHits hits = response.getHits();
            List<Student> list = new ArrayList<>((int) hits.getTotalHits().value);
            for (SearchHit hit : hits.getHits()) {
                String json = hit.getSourceAsString();
                list.add(JsonUtil.fromJson(json, Student.class));
            }
            return list;
        } catch (Exception e) {
            log.error("findByName error:", e);
        }
        return Collections.emptyList();
    }

    /**
     * 根据name查询
     */
    public List<Student> findByName(String index, String name) {
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery("name", name));

            SearchRequest request = new SearchRequest(index);
            request.source(searchSourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            SearchHits hits = response.getHits();
            List<Student> list = new ArrayList<>((int) hits.getTotalHits().value);
            for (SearchHit hit : hits.getHits()) {
                String json = hit.getSourceAsString();
                list.add(JsonUtil.fromJson(json, Student.class));
            }
            return list;
        } catch (Exception e) {
            log.error("findByName error:", e);
        }
        return Collections.emptyList();
    }

    /**
     * 关闭RestHighLevelClient
     */
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            log.error("close error:", e);
        }
    }
}
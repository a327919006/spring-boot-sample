package com.cn.boot.sample.es.util;

import com.cn.boot.sample.es.model.dto.StudentAddReq;
import com.cn.boot.sample.es.model.po.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.GetSourceRequest;
import org.elasticsearch.client.core.GetSourceResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.Min;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

        // （可选）设置账号密码，如果es未开启账号密码验证，则无需配置
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("elastic", "123456");
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
            httpAsyncClientBuilder.disableAuthCaching();
            return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        });

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
            Map<String, Object> nameFields = new HashMap<>();
            Map<String, Object> keyword = new HashMap<>();
            Map<String, Object> age = new HashMap<>();
            Map<String, Object> createTime = new HashMap<>();
            name.put("type", "text");
            name.put("fields", nameFields);
            nameFields.put("keyword", keyword);
            keyword.put("type", "keyword");
            keyword.put("ignore_above", 256);
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

    /**
     * 新增文档
     */
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

    /**
     * 批量新增文档（bulk本身支持批量操作，包括批量插入、更新、删除、混合等）
     */
    public boolean bulk(String index, List<StudentAddReq> list) {
        try {
            BulkRequest request = new BulkRequest(index);
            for (StudentAddReq req : list) {
                IndexRequest indexRequest = new IndexRequest(index);
                indexRequest.id(req.getId()).source(JsonUtil.toJson(req), XContentType.JSON);

                request.add(indexRequest);
            }


            BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
            return !response.hasFailures();
        } catch (Exception e) {
            log.error("save error:", e);
        }
        return false;
    }

    /**
     * 更新文档
     */
    public boolean update(String index, StudentAddReq req) {
        try {
            UpdateRequest request = new UpdateRequest(index, req.getId());
            request.doc(JsonUtil.toJson(req), XContentType.JSON);
            // 不存在时插入
            request.docAsUpsert(true);

            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
            if (response.getResult() == DocWriteResponse.Result.CREATED
                    || response.getResult() == DocWriteResponse.Result.UPDATED) {
                return true;
            }
        } catch (Exception e) {
            log.error("save error:", e);
        }
        return false;
    }

    /**
     * 删除文档
     */
    public boolean delete(String index, String id) {
        try {
            DeleteRequest request = new DeleteRequest(index, id);

            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
            return true;
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
     * 根据name查询
     */
    public List<Student> findByNameAndAge(String index, List<String> nameList, Integer age) {
        try {
            // 实现类似mysql的IN的效果
            // 方式一：使用bool+should
//            BoolQueryBuilder nameCondition = QueryBuilders.boolQuery();
//            nameCondition.minimumShouldMatch(1);
//            for (String name : nameList) {
//                nameCondition.should(QueryBuilders.matchQuery("name", name));
//            }
            // 方式二：termsQuery，注意：如果数据是大写英文（如AB12CD34），传入正确数据却查不到数据，可尝试转成小写后传入
            // 原因：此字段在es定义为text类型，会被分词，分词后索引中为小写（可调用/_analyze测试，索引中为ab12cd34）
            //      此时若传入原文AB12CD34进行term查询，term不会分词，所以按大写进行匹配，因此匹配不到数据
            TermsQueryBuilder nameCondition = QueryBuilders.termsQuery("name", nameList);

            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            if (!CollectionUtils.isEmpty(nameList)) {
                boolQuery.must(nameCondition);
            }
            if (null != age) {
                boolQuery.must(QueryBuilders.matchQuery("age", age));
            }

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(boolQuery);

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
     * 获取某个姓名的所有人的平均年龄
     *
     * @return
     */
    public Double getAvgAge(String index, String name) {
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            // 无需返回原始数据，只返回聚合结果
            searchSourceBuilder.size(0);

            // 过滤
            if (StringUtils.isNotEmpty(name)) {
                searchSourceBuilder.query(QueryBuilders.matchQuery("name", name));
            }

            // 聚合查询
            AvgAggregationBuilder aggregation = AggregationBuilders.avg("average_age").field("age");
            searchSourceBuilder.aggregation(aggregation);

            SearchRequest request = new SearchRequest(index);
            request.source(searchSourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            Avg avg = response.getAggregations().get("average_age");
            return avg.getValue();
        } catch (Exception e) {
            log.error("findByName error:", e);
        }
        return null;
    }

    /**
     * 按姓名分组，求每个名字的最大、最小年龄
     */
    public Map<String, String> getAgeGroupByName(String index) {
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            // 无需返回原始数据，只返回聚合结果
            searchSourceBuilder.size(0);

            // 聚合查询
            TermsAggregationBuilder aggregation = AggregationBuilders.terms("group_by_name").field("name.keyword");
            aggregation.subAggregation(AggregationBuilders.min("min_age").field("age"));
            aggregation.subAggregation(AggregationBuilders.max("max_age").field("age"));
            searchSourceBuilder.aggregation(aggregation);

            SearchRequest request = new SearchRequest(index);
            request.source(searchSourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            Terms terms = response.getAggregations().get("group_by_name");
            Map<String, String> result = new HashMap<>();
            for (Terms.Bucket bucket : terms.getBuckets()) {
                Min min = bucket.getAggregations().get("min_age");
                Max max = bucket.getAggregations().get("max_age");
                result.put(bucket.getKeyAsString(), min.getValue() + "->" + max.getValue());
            }
            return result;
        } catch (Exception e) {
            log.error("findByName error:", e);
        }
        return null;
    }

    /**
     * 滚动查询
     */
    public List<Student> scroll(String index, String name) {
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery("name", name));
            searchSourceBuilder.sort("_doc", SortOrder.DESC);
            searchSourceBuilder.size(3);

            Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1));
            SearchRequest request = new SearchRequest(index);
            request.scroll(scroll);
            request.source(searchSourceBuilder);
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            SearchHits hits = response.getHits();
            String scrollId = response.getScrollId();
            List<Student> list = new LinkedList<>();

            SearchHit[] searchHits = hits.getHits();
            while (searchHits != null && searchHits.length > 0) {
                for (SearchHit hit : searchHits) {
                    String json = hit.getSourceAsString();
                    list.add(JsonUtil.fromJson(json, Student.class));
                }

                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(scroll);
                response = client.scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = response.getScrollId();
                searchHits = response.getHits().getHits();
            }

            return list;
        } catch (Exception e) {
            log.error("scroll error:", e);
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

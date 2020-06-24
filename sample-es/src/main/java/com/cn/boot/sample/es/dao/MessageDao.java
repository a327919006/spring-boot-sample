package com.cn.boot.sample.es.dao;

import com.cn.boot.sample.es.model.po.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Chen Nan
 */
public interface MessageDao extends ElasticsearchRepository<Message, Long> {

    /**
     * 根据内容搜索
     */
    //@Query("{\"bool\" : {\"must\" : {\"field\" : {\"content\" : \"?\"}}}}")
    Page<Message> findByContent(String content, Pageable pageable);

    @Query("{\"bool\" : {\"must\" : {\"field\" : {\"user.keyword\" : \"?\"}}}}")
    Page<Message> findByUser(String firstCode, Pageable pageable);

}

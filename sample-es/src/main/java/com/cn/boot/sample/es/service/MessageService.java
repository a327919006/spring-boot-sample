package com.cn.boot.sample.es.service;

import com.cn.boot.sample.es.model.po.Message;
import org.springframework.data.domain.Page;

import java.util.Iterator;
import java.util.List;

/**
 * @author Chen Nan
 */
public interface MessageService {
    void createIndex();

    void deleteIndex(String index);

    void save(Message docBean);

    void saveAll(List<Message> list);

    Iterator<Message> findAll();

    Page<Message> findByContent(String content);

    Page<Message> findByUser(String firstCode);

    Page<Message> query(String key);
}

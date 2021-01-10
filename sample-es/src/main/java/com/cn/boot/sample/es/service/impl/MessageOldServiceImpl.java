package com.cn.boot.sample.es.service.impl;

import com.cn.boot.sample.es.dao.MessageDao;
import com.cn.boot.sample.es.model.po.Message;
import com.cn.boot.sample.es.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;

/**
 * @author Chen Nan
 */
@Service
public class MessageOldServiceImpl implements MessageService {

    private Pageable pageable = PageRequest.of(0, 10);

    @Autowired
    private ElasticsearchRestTemplate template;
    @Autowired
    private MessageDao dao;

    @Override
    public void createIndex() {
        template.createIndex(Message.class);
    }

    @Override
    public void deleteIndex() {
        template.deleteIndex(Message.class);
    }

    @Override
    public void save(Message docBean) {
        dao.save(docBean);
    }

    @Override
    public Iterator<Message> findAll() {
        return dao.findAll().iterator();
    }

    @Override
    public Page<Message> findByContent(String content) {
        return dao.findByContent(content, pageable);
    }

    @Override
    public Page<Message> findByUser(String firstCode) {
        return dao.findByUser(firstCode, pageable);
    }
}

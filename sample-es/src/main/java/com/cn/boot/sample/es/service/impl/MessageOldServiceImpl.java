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
    public boolean createIndex() {
        return false;
    }

    @Override
    public boolean deleteIndex() {
        return false;
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
    public Page<Message> findByContent(String content, int page, int size) {
        return dao.findByContent(content, PageRequest.of(page - 1, size));
    }

    @Override
    public Page<Message> findByUser(String firstCode, int page, int size) {
        return dao.findByUser(firstCode, PageRequest.of(page - 1, size));
    }
}

package com.cn.boot.sample.es.service.impl;

import com.cn.boot.sample.es.dao.MessageDao;
import com.cn.boot.sample.es.model.po.Message;
import com.cn.boot.sample.es.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;

/**
 * @author Chen Nan
 */
@Slf4j
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private MessageDao dao;

    @Override
    public boolean createIndex() {
        // SpringData会自动创建索引
        return true;
    }

    @Override
    public boolean deleteIndex() {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(Message.getIndexName());
            AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (Exception e) {
            log.error("deleteIndex error:", e);
        }
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

package com.cn.boot.sample.es.service;

import com.cn.boot.sample.es.model.po.Message;
import org.springframework.data.domain.Page;

import java.util.Iterator;

/**
 * @author Chen Nan
 */
public interface MessageService {
    /**
     * 创建索引
     *
     * @return 操作结果
     */
    boolean createIndex();

    /**
     * 删除索引
     *
     * @return 操作结果
     */
    boolean deleteIndex();

    /**
     * 保存消息
     *
     * @param message 消息内容
     */
    void save(Message message);

    /**
     * 获取所有消息
     *
     * @return 消息列表
     */
    Iterator<Message> findAll();

    /**
     * 根据内容搜索
     *
     * @param content 消息内容
     * @return 消息列表
     */
    Page<Message> findByContent(String content, int page, int size);

    /**
     * 根据发送人搜索
     *
     * @param user 发送人
     * @return 消息列表
     */
    Page<Message> findByUser(String user, int page, int size);
}

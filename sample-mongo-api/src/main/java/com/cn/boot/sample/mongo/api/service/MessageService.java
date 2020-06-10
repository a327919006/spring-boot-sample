package com.cn.boot.sample.mongo.api.service;


import com.cn.boot.sample.mongo.api.model.Message;
import com.cn.boot.sample.mongo.api.model.MessageCount;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Chen Nan
 */
public interface MessageService extends BaseService<Message, String> {

    /**
     * 插入消息-动态数据源
     *
     * @param appId   数据源key
     * @param message 消息内容
     * @return 操作结果
     */
    Message insertMessage(String appId, Message message);

    /**
     * 获取最近各个状态消息数量
     *
     * @param createTime 起始时间
     * @param count      最小数量
     * @return 数量列表
     */
    List<MessageCount> findMessageCount(LocalDateTime createTime, int count);
}

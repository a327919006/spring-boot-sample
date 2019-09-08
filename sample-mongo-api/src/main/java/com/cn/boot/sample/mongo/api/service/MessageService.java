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
     * 获取最近各个状态消息数量
     *
     * @param createTime 起始时间
     * @param count      最小数量
     * @return 数量列表
     */
    List<MessageCount> findMessageCount(LocalDateTime createTime, int count);
}

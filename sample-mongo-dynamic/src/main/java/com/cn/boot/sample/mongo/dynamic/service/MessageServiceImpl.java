package com.cn.boot.sample.mongo.dynamic.service;

import com.cn.boot.sample.mongo.api.model.Message;
import com.cn.boot.sample.mongo.api.model.MessageCount;
import com.cn.boot.sample.mongo.api.service.MessageService;
import com.cn.boot.sample.mongo.dynamic.config.dds.DynamicDataSource;
import com.cn.boot.sample.mongo.dynamic.dao.MessageDao;
import org.apache.dubbo.config.annotation.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Chen Nan
 */
@Service(timeout = 5000)
public class MessageServiceImpl extends BaseServiceImpl<MessageDao, Message, String>
        implements MessageService {


    @Override
    @DynamicDataSource
    public Message insertMessage(String appId, Message message) {
        return dao.insert(message);
    }

    @Override
    public List<MessageCount> findMessageCount(LocalDateTime createTime, int count) {
        return null;
    }
}

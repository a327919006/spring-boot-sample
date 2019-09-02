package com.cn.boot.sample.mongo.service;

import com.cn.boot.sample.mongo.api.model.Message;
import com.cn.boot.sample.mongo.api.model.MessageCount;
import com.cn.boot.sample.mongo.api.service.MessageService;
import com.cn.boot.sample.mongo.dao.MessageDao;
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
    public List<MessageCount> findMessageCount(LocalDateTime createTime, int count) {
        return dao.findMessageCount(createTime, count);
    }
}

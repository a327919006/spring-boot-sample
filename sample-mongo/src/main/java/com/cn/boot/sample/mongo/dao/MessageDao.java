package com.cn.boot.sample.mongo.dao;

import com.cn.boot.sample.mongo.api.model.Message;
import org.springframework.stereotype.Repository;

/**
 * @author Chen Nan
 */
@Repository
public class MessageDao extends BaseMongoDao<Message, String> {

    @Override
    Class<Message> getEntityClass() {
        return Message.class;
    }
}

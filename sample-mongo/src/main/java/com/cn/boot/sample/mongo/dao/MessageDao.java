package com.cn.boot.sample.mongo.dao;

import com.cn.boot.sample.mongo.api.model.Message;
import com.cn.boot.sample.mongo.api.model.MessageCount;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Chen Nan
 */
@Repository
public class MessageDao extends BaseMongoDao<Message, String> {

    @Override
    Class<Message> getEntityClass() {
        return Message.class;
    }

    /**
     * 获取最近各个状态消息数量
     * db.getCollection('messages').aggregate(
     * {$match:{createTime:{$gte:xxxxxxxxxx}, content:{$ne:''}}},
     * {$group:{_id:"$status", count:{$sum:1}}},
     * {$match:{count:{$gte:xxxx}}}
     * )
     *
     * @param createTime 起始时间
     * @param count      最小数量
     * @return 数量列表
     */
    public List<MessageCount> findMessageCount(LocalDateTime createTime, int count) {
        TypedAggregation<Message> aggregation = TypedAggregation.newAggregation(
                Message.class,
                TypedAggregation.match(Criteria.where("createTime").gte(createTime).and("content").ne("")),
                TypedAggregation.group("status").count().as("count"),
                TypedAggregation.match(Criteria.where("count").gte(count)));

        AggregationResults<MessageCount> results = mongoTemplate.aggregate(aggregation, MessageCount.class);
        return results.getMappedResults();
    }
}

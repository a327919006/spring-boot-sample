package com.cn.boot.sample.mongo.dao;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Chen Nan
 */
@SuppressWarnings("unchecked")
@Slf4j
public abstract class BaseMongoDao<T, PK> {
    @Autowired
    public MongoTemplate mongoTemplate;

    /**
     * 反射获取泛型类型
     *
     * @return 泛型类型
     */
    abstract Class<T> getEntityClass();

    /**
     * 根据主键删除
     *
     * @param id 根据主键删除
     */
    public int deleteByPrimaryKey(PK id) {
        return (int) mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), getEntityClass()).getDeletedCount();
    }

    /**
     * 插入数据
     */
    public T insert(T record) {
        return mongoTemplate.insert(record);
    }

    /**
     * 根据主键查询
     */
    public T selectByPrimaryKey(PK id) {
        return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), getEntityClass());
    }

    /**
     * 根据主键更新数据
     */
    public int updateByPrimaryKey(T record) {
        PK id = null;
        Update update = new Update();
        Field[] fields = ReflectUtil.getFields(getEntityClass());
        for (Field field : fields) {
            if (StringUtils.endsWithIgnoreCase(field.getName(), "id")
                    || StringUtils.endsWithIgnoreCase(field.getName(), "_id")
                    || field.getAnnotation(Id.class) != null) {
                id = (PK) ReflectUtil.getFieldValue(record, field.getName());
            } else {
                Object fieldValue = ReflectUtil.getFieldValue(record, field.getName());
                if (fieldValue != null) {
                    update.set(field.getName(), fieldValue);
                }
            }
        }
        if (id == null) {
            throw new IllegalArgumentException("unknown id file name");
        }
        Query query = Query.query(Criteria.where("_id").is(id));
        return (int) mongoTemplate.updateFirst(query, update, getEntityClass()).getModifiedCount();
    }

    /**
     * 获取数量
     */
    public int count(T record) {
        return (int) mongoTemplate.count(Query.query(Criteria.byExample(record)), getEntityClass());
    }

    /**
     * 获取单条数据
     */
    public T get(T record) {
        return mongoTemplate.findOne(Query.query(Criteria.byExample(record)), getEntityClass());
    }

    /**
     * 获取列表
     */
    public List<T> list(T record) {
        return mongoTemplate.find(Query.query(Criteria.byExample(record)), getEntityClass());
    }
}

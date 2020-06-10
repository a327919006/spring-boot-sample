package com.cn.boot.sample.mongo.dynamic.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Chen Nan
 */
@Component
public class MongoTemplateUtil {

    @Autowired
    private Map<String, MongoTemplate> mongoTemplateMap;

    public MongoTemplate getMongoTemplate(String key) {
        return mongoTemplateMap.get(key);
    }

    public boolean isExistDataSource(String key) {
        return mongoTemplateMap.containsKey(key);
    }
}

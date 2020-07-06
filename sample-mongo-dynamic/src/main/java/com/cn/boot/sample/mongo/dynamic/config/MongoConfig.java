package com.cn.boot.sample.mongo.dynamic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

/**
 * @author Chen Nan
 */
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.db1.uri}")
    private String db1Uri;
    @Value("${spring.data.mongodb.db2.uri}")
    private String db2Uri;

    @Bean
    @Primary
    public MongoDatabaseFactory db1MongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory(db1Uri);
    }

    @Bean
    public MongoDatabaseFactory db2MongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory(db2Uri);
    }

    @Bean
    @Primary
    public MongoTemplate ds1() {
        return new MongoTemplate(db1MongoDbFactory());
    }

    @Bean
    public MongoTemplate ds2() {
        return new MongoTemplate(db2MongoDbFactory());
    }
}

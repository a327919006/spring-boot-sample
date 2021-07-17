package com.cn.boot.sample.jackson.config;


import com.cn.boot.sample.jackson.config.serializer.LocalDateTimeDeserializer;
import com.cn.boot.sample.jackson.config.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * 默认JacksonAutoConfiguration会初始化ObjectMapper
 * 下方代码在原有配置基础上增加自定义配置（原有配置会读取yaml中的配置项）,比如自定义LocalDateTime序列化方式
 *
 * @author Chen Nan
 */
@Configuration
public class JacksonConfig {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 继续使用
     */
    @PostConstruct
    public void init() {
        // 基础配置可在yaml中定义，也可在代码中配置
        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 设置全局的LocalDateTime序列化与反序列化
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
        simpleModule.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
        objectMapper.registerModule(simpleModule);
    }
}
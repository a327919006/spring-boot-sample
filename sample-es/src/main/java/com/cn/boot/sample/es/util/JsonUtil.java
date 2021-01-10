package com.cn.boot.sample.es.util;

import com.cn.boot.sample.api.exceptions.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Chen Nan
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("toJson error:", e);
            throw new BusinessException("toJson error:", e);
        }
    }

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.error("toJson error:", e);
            throw new BusinessException("fromJson error:", e);
        }
    }
}

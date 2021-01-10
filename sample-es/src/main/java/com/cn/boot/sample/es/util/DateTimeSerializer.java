package com.cn.boot.sample.es.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p>Title: DateTimeSerializer</p>
 * <p>Description: 自定义返回JSON时间格式化处理 </p>
 */
public class DateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime date, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if (date != null) {
            jsonGenerator.writeString(date.toString());
        } else {
            jsonGenerator.writeString("");
        }
    }
}

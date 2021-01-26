package com.cn.boot.sample.es.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p>自定义返回JSON时间格式化处理 </p>
 */
public class DateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser parser,
                                     DeserializationContext context) throws IOException {
        String src = parser.getText();
        if (StringUtils.isNotBlank(src)) {
            return LocalDateTime.parse(src);
        }
        return null;
    }
}

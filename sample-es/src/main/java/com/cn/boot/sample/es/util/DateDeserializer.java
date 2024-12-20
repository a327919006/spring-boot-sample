package com.cn.boot.sample.es.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Date;

/**
 * <p>自定义返回JSON时间格式化处理 </p>
 */
public class DateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String src = parser.getText();
        if (StringUtils.isNotBlank(src)) {
            return DateUtil.parse(src, DatePattern.NORM_DATETIME_MS_FORMAT).toJdkDate();
        }
        return null;
    }
}

package com.cn.boot.sample.graphql.subscribe.util;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

/**
 * @author Chen Nan
 */
public class JsonKit {

    public static void toJson(HttpServletResponse response, Object result) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.write(JSONUtil.toJsonStr(result));
        writer.flush();
        writer.close();
    }

    public static Map<String, Object> toMap(String jsonStr) {
        if (jsonStr == null || jsonStr.trim().length() == 0) {
            return Collections.emptyMap();
        }
        // gson uses type tokens for generic input like Map<String,Object>
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
        };
        Map<String, Object> map = JSONUtil.toBean(jsonStr, typeReference, true);
        return map == null ? Collections.emptyMap() : map;
    }

    public static String toJsonString(Object obj) {
        return JSONUtil.toJsonStr(obj);
    }
}

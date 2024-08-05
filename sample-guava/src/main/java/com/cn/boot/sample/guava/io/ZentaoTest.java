package com.cn.boot.sample.guava.io;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


/**
 * 文件操作
 *
 * @author Chen Nan
 */
@Slf4j
@TestInstance(PER_CLASS)
public class ZentaoTest {

    private static CookieStore cookieStore;

    @BeforeAll
    public void init() throws FileNotFoundException {
        String session = doGet("http://192.168.1.221:82/zentao/api-getsessionid.json", null, false);
        JSONObject sessionJSON = JSONObject.parseObject(session);
        // log.info("session: {}", sessionJSON.toJSONString());

        JSONObject sessionDataJSON = sessionJSON.getJSONObject("data");
        String sessionID = sessionDataJSON.getString("sessionID");
        // log.info("sessionID: {}", sessionID);

        Map<String, Object> map = new HashMap<>();
        map.put("account", "followup");
        map.put("password", "followup");
        map.put("zentaosid", sessionID);
        doGet("http://192.168.1.221:82/zentao/user-login.json", map, true);
    }

    public static String doGet(String url, Map<String, Object> params, boolean isCookie) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (null != params) {
                for (String key : params.keySet()) {
                    builder.setParameter(key, String.valueOf(params.get(key)));
                }
            }
            HttpGet get = new HttpGet(builder.build());
            if (!isCookie) {
                response = httpclient.execute(get);
                System.out.println(response.getStatusLine());
                if (200 == response.getStatusLine().getStatusCode()) {
                    HttpEntity entity = response.getEntity();
                    resultString = EntityUtils.toString(entity, "utf-8");
                }
            } else {
                resultString = GetCookies(get);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != httpclient) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultString;
    }


    public static String GetCookies(HttpGet get) throws IOException {
        String result = null;
        try {
            if (null == cookieStore) {
                cookieStore = new BasicCookieStore();
            }
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            CloseableHttpResponse response = httpClient.execute(get);
            result = EntityUtils.toString(response.getEntity(), "utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Test
    public void testZentao() throws IOException {
        System.out.println(123);
    }

}

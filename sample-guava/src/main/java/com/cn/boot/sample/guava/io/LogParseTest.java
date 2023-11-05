package com.cn.boot.sample.guava.io;

import com.alibaba.fastjson.JSONArray;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


/**
 * 文件操作
 *
 * @author Chen Nan
 */
@Slf4j
@TestInstance(PER_CLASS)
public class LogParseTest {

    private File sourceFile;
    private String format = "yyyy-MM-dd HH:mm:ss.SSS";

    @BeforeAll
    public void init() throws FileNotFoundException {
        String sourceFilePath = "log-20231105 215200.json";
        sourceFile = ResourceUtils.getFile("classpath:" + sourceFilePath);
    }

    /**
     * 按行处理
     */
    @Test
    public void readLines() throws IOException {
        String logStr = Files.asCharSource(sourceFile, StandardCharsets.UTF_8).read();
        List<LogLine> logLines = JSONArray.parseArray(logStr, LogLine.class);
        Map<String, LogTime> pathMap = new HashMap<>();
        for (LogLine logLine : logLines) {
            LogLine.FieldsBean fields = logLine.getFields();
            String request = fields.getRequest_uri();
            String[] split = StringUtils.split(request, " ");
            String requestUri = split[1];
            double requestTime = Double.parseDouble(fields.getRequest_time());
            String time = DateFormatUtils.format(Long.parseLong(logLine.getTimestamp().substring(0, 13)), format);
            if (!pathMap.containsKey(requestUri)) {
                pathMap.put(split[1], new LogTime(requestTime, time, 1));
            } else {
                LogTime currTime = pathMap.get(requestUri);
                int times = currTime.getTimes() + 1;
                if (requestTime < currTime.getCost()) {
                    pathMap.put(requestUri, new LogTime(requestTime, time, times));
                }
            }
        }
        // log.info("result={}", pathMap);
        pathMap.forEach((path, lineTime) -> {
            System.out.println(path + "\t" + lineTime.getDatetime() + "\t" + lineTime.getCost() + "\t" + lineTime.getTimes());
        });
    }
}

package com.cn.boot.sample.guava.io;

import com.alibaba.fastjson.JSONArray;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


/**
 * 文件操作
 *
 * @author Chen Nan
 */
@Slf4j
@TestInstance(PER_CLASS)
public class BlackListParseTest {

    private File sourceFile;

    @BeforeAll
    public void init() throws FileNotFoundException {
        String sourceFilePath = "black.json";
        sourceFile = ResourceUtils.getFile("classpath:" + sourceFilePath);
    }

    /**
     * 按行处理
     */
    @Test
    public void readLines() throws IOException {
        String logStr = Files.asCharSource(sourceFile, StandardCharsets.UTF_8).read();
        List<Black> blackList = JSONArray.parseArray(logStr, Black.class);
        Set<String> blackSet = new HashSet<>();
        for (Black black : blackList) {
            String blacklistPro = black.getBlacklistPro();
            if (StringUtils.isEmpty(blacklistPro)) {
                continue;
            }
            List<String> blackArray = Arrays.asList(StringUtils.split(blacklistPro, ","));
            blackSet.addAll(blackArray);
        }
        for (String black : blackSet) {
            System.out.println(black);
        }
    }
}

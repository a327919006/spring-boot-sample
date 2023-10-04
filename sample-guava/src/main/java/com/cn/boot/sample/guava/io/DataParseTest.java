package com.cn.boot.sample.guava.io;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
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
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


/**
 * 文件操作
 *
 * @author Chen Nan
 */
@Slf4j
@TestInstance(PER_CLASS)
public class DataParseTest {

    private File file;

    @BeforeAll
    public void init() throws FileNotFoundException {
        file = ResourceUtils.getFile("classpath:test.csv");
    }

    /**
     * 按行处理
     */
    @Test
    public void readLines() throws IOException {
        LineProcessor<List<String>> lineProcessor = new LineProcessor<List<String>>() {

            private final List<String> result = new ArrayList<>();

            /**
             *
             * @param line 每行数据
             * @return true：继续往下读，false：结束
             */
            @Override
            public boolean processLine(String line) throws IOException {
                if (StrUtil.isBlank(line)) {
                    return false;
                }
                if (StringUtils.startsWith(line, "timestamp")) {
                    result.add(line);
                    return true;
                }
                if (result.size() == 1) {
                    result.add(line);
                    return true;
                }

                String[] split = line.split(",");
                String timeStr = split[0];
                DateTime date = DateUtil.parseDateTime(timeStr);

                return true;
            }

            @Override
            public List<String> getResult() {
                return result;
            }
        };

        List<String> result = Files.asCharSource(file, StandardCharsets.UTF_8).readLines(lineProcessor);
        // [test1_test, test2_test]
        log.info("result = {}", result);
    }

    /**
     * 文件追加内容
     */
    @Test
    public void append() throws IOException {
        String absolutePath = file.getAbsolutePath();
        log.info(absolutePath);

        CharSink charSink = Files.asCharSink(file, StandardCharsets.UTF_8, FileWriteMode.APPEND);
        charSink.write("testAppend1");
        charSink.write("testAppend2");

        String result = Files.asCharSource(file, StandardCharsets.UTF_8).read();
        log.info("result = {}", result);
    }

}

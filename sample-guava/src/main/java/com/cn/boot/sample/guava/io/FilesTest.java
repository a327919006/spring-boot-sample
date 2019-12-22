package com.cn.boot.sample.guava.io;

import cn.hutool.core.util.StrUtil;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
public class FilesTest {

    private File file;

    @BeforeAll
    public void init() throws FileNotFoundException {
        file = ResourceUtils.getFile("classpath:test.txt");
    }

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
                result.add(line + "_test");
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

    @Test
    public void fileHash() throws IOException {
        HashCode hash1 = Files.asByteSource(file).hash(Hashing.goodFastHash(128));
        HashCode hash2 = Files.asByteSource(file).hash(Hashing.sha256());
        // efa4cce1211e08ba5887badabcf65245
        log.info("hash1 = {}", hash1);
        // f040b8bb953648986df69f60a383199aa04b2ec0f0c4e50db870ee863419fd8d
        log.info("hash2 = {}", hash2);
    }

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

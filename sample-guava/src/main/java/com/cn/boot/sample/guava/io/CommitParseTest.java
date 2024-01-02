package com.cn.boot.sample.guava.io;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
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
import java.util.*;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


/**
 * 文件操作
 *
 * @author Chen Nan
 */
@Slf4j
@TestInstance(PER_CLASS)
public class CommitParseTest {

    private File sourceFile;

    @BeforeAll
    public void init() throws FileNotFoundException {
        String sourceFilePath = "commit_info.txt";
        sourceFile = ResourceUtils.getFile("classpath:" + sourceFilePath);
    }

    /**
     * 按行处理
     */
    @Test
    public void readLines() throws IOException {
        LineProcessor<String> lineProcessor = new LineProcessor<String>() {

            private final Map<String, CommitInfo> featMap = new HashMap<>();
            private final Map<String, CommitInfo> bugMap = new HashMap<>();

            @Override
            public boolean processLine(String line) throws IOException {
                if (StrUtil.isBlank(line)) {
                    return true;
                }
                String[] infoArray = line.split(" - ");
                String commitId = infoArray[0];
                String user = infoArray[1];
                String timeStr = infoArray[2];
                String msg = infoArray[3];
                System.out.println(commitId);
                System.out.println(user);
                System.out.println(timeStr);
                System.out.println(msg);

                String type = StringUtils.substringBefore(msg, ":");
                System.out.println(type);
                if (!StringUtils.equalsAny(type, "feat", "fix")) {
                    return true;
                }
                String numStr = StringUtils.substringBetween(msg, "[", "]");
                System.out.println(numStr);
                String[] numArray = StringUtils.split(numStr, ",");
                for (String num : numArray) {
                    if (StringUtils.equals(type, "feat")) {
                        CommitInfo commitInfo = featMap.get(num);
                        if (commitInfo == null) {

                        } else {

                        }
                    } else {

                    }
                }
                return true;
            }

            @Override
            public String getResult() {
                return "";
            }
        };

        Files.asCharSource(sourceFile, StandardCharsets.UTF_8).readLines(lineProcessor);
    }


}

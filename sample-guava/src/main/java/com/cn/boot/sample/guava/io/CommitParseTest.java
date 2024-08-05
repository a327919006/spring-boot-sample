package com.cn.boot.sample.guava.io;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


@Slf4j
@TestInstance(PER_CLASS)
public class CommitParseTest {

    public static String typeFeat = "feat";
    public static String typeFix = "fix";
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
            private final Map<String, CommitInfo> fixMap = new HashMap<>();

            @Override
            public boolean processLine(String line) throws IOException {
                if (StrUtil.isBlank(line)) {
                    return true;
                }
                String[] infoArray = line.split(" - ");
                String commitId = infoArray[0];
                String author = infoArray[1];
                String timeStr = StringUtils.substringBefore(infoArray[2], " +");
                String msg = infoArray[3];
//                System.out.println(commitId);
//                System.out.println(author);
//                System.out.println(timeStr);
//                System.out.println(msg);

                String type = StringUtils.substringBefore(msg, ":");
//                System.out.println(type);
                if (!StringUtils.equalsAny(type, typeFeat, typeFix)) {
                    return true;
                }
                String numStr = StringUtils.substringBetween(msg, "[", "]");
//                System.out.println(numStr);
                String[] numArray = StringUtils.split(numStr, ",");
                for (String num : numArray) {
                    CommitInfo commitInfo;
                    if (StringUtils.equals(type, typeFeat)) {
                        commitInfo = featMap.get(num);
                    } else {
                        commitInfo = fixMap.get(num);
                    }
                    if (commitInfo == null) {
                        commitInfo = new CommitInfo();
                        commitInfo.setType(type);
                        commitInfo.setNum(num);
                        commitInfo.setAuthorList(CollectionUtil.newHashSet(author));
                        commitInfo.setFirst(DateUtil.parse(timeStr));
                        commitInfo.setLast(DateUtil.parse(timeStr));
                    } else {
                        commitInfo.getAuthorList().add(author);
                        commitInfo.setFirst(DateUtil.parse(timeStr));
                    }
                    if (StringUtils.equals(type, typeFeat)) {
                        featMap.put(num, commitInfo);
                    } else {
                        fixMap.put(num, commitInfo);
                    }
                }
                return true;
            }

            @Override
            public String getResult() {
//                System.out.println(featMap);
//                System.out.println(fixMap);
                for (String num : featMap.keySet()) {
                    System.out.println(featMap.get(num));
                }
                for (String num : fixMap.keySet()) {
                    System.out.println(fixMap.get(num));
                }
                return "";
            }
        };

        Files.asCharSource(sourceFile, StandardCharsets.UTF_8).readLines(lineProcessor);
    }


}

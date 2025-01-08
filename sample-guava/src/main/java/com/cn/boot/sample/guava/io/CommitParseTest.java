package com.cn.boot.sample.guava.io;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
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
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


@Slf4j
@TestInstance(PER_CLASS)
public class CommitParseTest {

    public static String typeFeat = "feat";
    public static String typeFix = "fix";
    private File commitFile;
    private File planFeatFile;
    private File planFixFile;
    private final Comparator<Integer> reverseOrderComparator = Collections.reverseOrder();
    private final Set<Integer> commitFeatSet = new TreeSet<>(reverseOrderComparator);
    private final Set<Integer> commitFixSet = new TreeSet<>(reverseOrderComparator);
    private final Set<Integer> planFeatSet = new TreeSet<>(reverseOrderComparator);
    private final Set<Integer> planFixSet = new TreeSet<>(reverseOrderComparator);

    @BeforeAll
    public void init() throws FileNotFoundException {
        String commitFilePath = "commit_info.txt";
        String planFeatFilePath = "commit_plan_feat.txt";
        String planFixFilePath = "commit_plan_fix.txt";
        commitFile = ResourceUtils.getFile("classpath:" + commitFilePath);
        planFeatFile = ResourceUtils.getFile("classpath:" + planFeatFilePath);
        planFixFile = ResourceUtils.getFile("classpath:" + planFixFilePath);
    }

    /**
     * 按行处理
     */
    @Test
    public void readLines() throws IOException {
        commitParse();
        planFeatParse();
        planFixParse();
        System.out.println("计划BUG：" + StrUtil.join(",", planFixSet));
        System.out.println("提交BUG：" + StrUtil.join(",", commitFixSet));
        System.out.println("计划需求：" + StrUtil.join(",", planFeatSet));
        System.out.println("提交需求：" + StrUtil.join(",", commitFeatSet));

        Set<Integer> errorFeatSet = new TreeSet<>(reverseOrderComparator);
        Set<Integer> errorFixSet = new TreeSet<>(reverseOrderComparator);
        for (Integer num : commitFixSet) {
            if (!planFixSet.contains(num)) {
                errorFixSet.add(num);
            }
        }
        for (Integer num : commitFeatSet) {
            if (!planFeatSet.contains(num)) {
                errorFeatSet.add(num);
            }
        }

        System.out.println("未规划BUG：" + StrUtil.join(",", errorFixSet));
        System.out.println("未规划需求：" + StrUtil.join(",", errorFeatSet));
    }

    private void commitParse() throws IOException {
        LineProcessor<String> commitProcessor = new LineProcessor<String>() {

            private final Comparator<Integer> reverseOrderComparator = Collections.reverseOrder();

            private final Map<Integer, CommitInfo> featMap = new TreeMap<>(reverseOrderComparator);
            private final Map<Integer, CommitInfo> fixMap = new TreeMap<>(reverseOrderComparator);

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
                    int numInt = Integer.parseInt(num);
                    CommitInfo commitInfo;
                    if (StringUtils.equals(type, typeFeat)) {
                        commitInfo = featMap.get(numInt);
                    } else {
                        commitInfo = fixMap.get(numInt);
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
                        featMap.put(numInt, commitInfo);
                    } else {
                        if (numInt < 2000) {
                            continue;
                        }
                        fixMap.put(numInt, commitInfo);
                    }
                }
                return true;
            }

            @Override
            public String getResult() {
//                System.out.println(featMap);
//                System.out.println(fixMap);
                for (Integer num : featMap.keySet()) {
//                    System.out.println(featMap.get(num));
                    commitFeatSet.add(num);
                }
                for (Integer num : fixMap.keySet()) {
//                    System.out.println(fixMap.get(num));
                    commitFixSet.add(num);
                }
                return "";
            }
        };

        Files.asCharSource(commitFile, StandardCharsets.UTF_8).readLines(commitProcessor);
    }

    private void planFeatParse() throws IOException {
        LineProcessor<String> planProcessor = new LineProcessor<String>() {

            private final AtomicInteger lineNum = new AtomicInteger();

            @Override
            public boolean processLine(String line) throws IOException {
                if (lineNum.getAndIncrement() % 3 != 0) {
                    return true;
                }
//                System.out.println(line);
                planFeatSet.add(Integer.parseInt(line));
                return true;
            }

            @Override
            public String getResult() {
                return "";
            }
        };

        Files.asCharSource(planFeatFile, StandardCharsets.UTF_8).readLines(planProcessor);
    }

    private void planFixParse() throws IOException {
        LineProcessor<String> planProcessor = new LineProcessor<String>() {

            private final AtomicInteger lineNum = new AtomicInteger();

            @Override
            public boolean processLine(String line) throws IOException {
                if (lineNum.getAndIncrement() % 3 != 0) {
                    return true;
                }
//                System.out.println(line);
                planFixSet.add(Integer.parseInt(line));
                return true;
            }

            @Override
            public String getResult() {
                return "";
            }
        };

        Files.asCharSource(planFixFile, StandardCharsets.UTF_8).readLines(planProcessor);
    }
}

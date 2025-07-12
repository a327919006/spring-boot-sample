package com.cn.boot.sample.guava.io;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


@Slf4j
@TestInstance(PER_CLASS)
public class PingParseTest {

    public static Integer num = 1;
    private static final Map<Integer, Integer> lateMap = new TreeMap<>();

    /**
     * 按行处理
     */
    @Test
    public void readLines() throws IOException {

        File sourceFile;
        for (int i = 1; i <= 24; i++) {
            num = i;
            String filename = "ping1" + StrUtil.padPre(num.toString(), 2, '0') + ".txt";
            sourceFile = ResourceUtils.getFile("classpath:" + filename);
            Files.asCharSource(sourceFile, StandardCharsets.UTF_8).readLines(createLineProcessor());
        }

        for (Integer hour : lateMap.keySet()) {
            System.out.println(hour + "点\t" + lateMap.get(hour) );
        }

        num = 252;
        sourceFile = ResourceUtils.getFile("classpath:ping252.txt");
        Files.asCharSource(sourceFile, StandardCharsets.UTF_8).readLines(createLineProcessor());

        num = 253;
        sourceFile = ResourceUtils.getFile("classpath:ping253.txt");
        Files.asCharSource(sourceFile, StandardCharsets.UTF_8).readLines(createLineProcessor());
    }

    private LineProcessor createLineProcessor() {
        return new LineProcessor<String>() {

            private final Map<Integer, Integer> costMap = new TreeMap<>();
            private float lateCount = 0f;
            private float totalCount = 0f;

            @Override
            public boolean processLine(String line) throws IOException {
                if (StrUtil.isBlank(line)) {
                    return true;
                }
                if (!StrUtil.contains(line, "时间")) {
                    if (StrUtil.contains(line, "超时")) {
                        log.error("line={}", line);
                    }
                    return true;
                }
                String costStr = StrUtil.subBetween(line, "字节=32 ", " TTL=");
                Integer cost = 0;
                totalCount++;
                if (!StrUtil.equals("时间<1ms", costStr)) {
                    lateCount++;
                    String name = "号桩";
                    if (num >= 252) {
                        name = "串口服务器";
                    }
                    String timeStr = StrUtil.subBefore(line, " - ", true);
                    cost = Integer.parseInt(StrUtil.subBetween(costStr, "时间", "ms").substring(1));
                    Integer hour = Integer.parseInt(StrUtil.subBetween(timeStr," ", ":"));
                    Integer count = 1;
                    if (lateMap.containsKey(hour)) {
                        count = lateMap.get(hour);
                        count++;
                    }
                    lateMap.put(hour, count);
//                    System.out.println(num + name + "\t" + timeStr + "\t" + costStr);
                }

                Integer count = 1;
                if (costMap.containsKey(cost)) {
                    count = costMap.get(cost);
                    count++;
                }
                costMap.put(cost, count);
                return true;
            }

            @Override
            public String getResult() {
                String name = "号桩";
                if (num >= 252) {
                    name = "串口服务器";
                }
                BigDecimal rate = NumberUtil.round(lateCount / totalCount, 5);
//                System.out.println(num + name + "\t"
//                        + (int)totalCount + "次\t"
//                        + (int)lateCount + "次\t'"
//                        + rate + "\t");
                for (Integer cost : costMap.keySet()) {
//                    System.out.println(num + name + "\t" + cost + "ms\t" + costMap.get(cost) + "次");
                }
                return "";
            }
        };
    }


}

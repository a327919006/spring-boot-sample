package com.cn.boot.sample.guava.io;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


@Slf4j
@TestInstance(PER_CLASS)
public class PingParseTest {

    public static Integer num = 1;

    /**
     * 按行处理
     */
    @Test
    public void readLines() throws IOException {

        File sourceFile;
        for (int i = 1; i <= 2; i++) {
            num = i;
            String filename = "ping1" + StrUtil.padPre(num.toString(), 2, '0') + ".txt";
            sourceFile = ResourceUtils.getFile("classpath:" + filename);
            Files.asCharSource(sourceFile, StandardCharsets.UTF_8).readLines(new LineProcessor<String>() {

                private final Map<String, Integer> costMap = new HashMap<>();

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
                    String costStr = StrUtil.subBetween(line, "字节=32 ", " TTL=255");
                    if (!StrUtil.equals("时间<1ms", costStr)) {
//                        System.out.println(line);
                    }
                    Integer count = 0;
                    if (costMap.containsKey(costStr)) {
                        count = costMap.get(costStr);
                        count++;
                    }
                    costMap.put(costStr, count);
                    return true;
                }

                @Override
                public String getResult() {
                    for (String cost : costMap.keySet()) {
                        System.out.println(num + "号桩\t" + cost + "\t" + costMap.get(cost) + "次");
                    }
                    return "";
                }
            });
        }
    }


}

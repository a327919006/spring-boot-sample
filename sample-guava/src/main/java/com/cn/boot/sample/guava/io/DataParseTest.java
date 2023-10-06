package com.cn.boot.sample.guava.io;

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

    private File sourceFile;
    private File sinkFile;

    @BeforeAll
    public void init() throws FileNotFoundException {
        // String sourceFilePath = "train_temp.csv";
        // String sourceFilePath = "test_temp.csv";
        // String sourceFilePath = "train_hudi.csv";
        String sourceFilePath = "test_hudi.csv";
        sourceFile = ResourceUtils.getFile("classpath:" + sourceFilePath);
        String sinkFilePath = sourceFile.getParent() + "/result.csv";
        FileUtil.del(sinkFilePath);
        sinkFile = FileUtil.newFile(sinkFilePath);
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

                long currTime = getLineTime(line);
                String lastLine = result.get(result.size() - 1);
                String[] lastData = lastLine.split(",");
                long lastTime = getLineTime(lastLine);
                while (currTime - lastTime > 60 * 1000) {
                    String fillTime = DateUtil.formatDateTime(new Date(lastTime + 60 * 1000));
                    String fillLine = fillTime + "," + lastData[1] + "," + lastData[2];
                    log.info("fillLine={}", fillLine);
                    result.add(fillLine);
                    lastTime = lastTime + 60 * 1000;
                }
                result.add(line);
                return true;
            }

            @Override
            public List<String> getResult() {
                return result;
            }
        };

        List<String> result = Files.asCharSource(sourceFile, StandardCharsets.UTF_8).readLines(lineProcessor);
        // log.info("result = {}", result);
        CharSink charSink = Files.asCharSink(sinkFile, StandardCharsets.UTF_8, FileWriteMode.APPEND);
        charSink.writeLines(result);
    }

    private long getLineTime(String line) {
        String[] split = line.split(",");
        String timeStr = split[0];
        return DateUtil.parseDateTime(timeStr).getTime();
    }

}

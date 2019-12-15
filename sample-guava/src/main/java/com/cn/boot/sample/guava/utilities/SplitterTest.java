package com.cn.boot.sample.guava.utilities;

import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
public class SplitterTest {

    @Test
    public void onSplitter() {
        String data = "aaa|bbb";
        List<String> result = Splitter.on("|").splitToList(data);
        // [aaa, bbb]
        log.info("result={}", result);
    }

    @Test
    public void onSplitterTrim() {
        String data = " aaa|bbb ";
        List<String> result = Splitter.on("|").trimResults().splitToList(data);
        // [aaa, bbb]
        log.info("result={}", result);
    }

    @Test
    public void onSplitterOmitEmpty() {
        String data = "aaa|bbb|||||";
        List<String> result = Splitter.on("|").omitEmptyStrings().splitToList(data);
        // [aaa, bbb]
        log.info("result={}", result);
    }

    @Test
    public void onSplitterFixedLength() {
        String data = "aaabbb";
        List<String> result = Splitter.fixedLength(3).splitToList(data);
        // [aaa, bbb]
        log.info("result={}", result);
    }

    @Test
    public void onSplitterLimit() {
        String data = "aaa|bbb|ccc|ddd";
        List<String> result = Splitter.on("|").limit(2).splitToList(data);
        // [aaa, bbb|ccc|ddd]
        log.info("result={}", result);
    }

    @Test
    public void onSplitterOnPattern() {
        String data = "aaa|bbb|ccc|ddd";
        List<String> result = Splitter.onPattern("\\|").splitToList(data);
        // [aaa, bbb, ccc, ddd]
        log.info("result={}", result);
    }

    @Test
    public void onSplitterToMap() {
        String data = "aaa=1|bbb=2|ccc=3";
        Map<String, String> result = Splitter.on("|").withKeyValueSeparator('=').split(data);
        // {aaa=1, bbb=2, ccc=3}
        log.info("result={}", result);
    }

}

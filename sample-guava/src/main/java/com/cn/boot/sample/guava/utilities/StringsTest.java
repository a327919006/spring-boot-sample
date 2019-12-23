package com.cn.boot.sample.guava.utilities;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Chen Nan
 */
@Slf4j
public class StringsTest {

    @Test
    public void commonPrefix() {
        // 共同前缀
        String result = Strings.commonPrefix("12345", "12222");
        // 12
        log.info("result={}", result);
    }

    @Test
    public void commonSuffix() {
        // 共同后缀
        String result = Strings.commonSuffix("12345", "00045");
        // 45
        log.info("result={}", result);
    }

    @Test
    public void repeat() {
        // 复制成3份
        String result = Strings.repeat("123", 3);
        // 123123123
        log.info("result={}", result);
    }

    @Test
    public void padStart() {
        // 前补0
        String result = Strings.padStart("123", 5, '0');
        // 00123
        log.info("result={}", result);
    }

    @Test
    public void padEnd() {
        // 后补0
        String result = Strings.padEnd("123", 5, '0');
        // 12300
        log.info("result={}", result);
    }
}

package com.cn.boot.sample.guava.utilities;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chen Nan
 */
@Slf4j
public class JoinerTest {

    private final List<String> list1 = Arrays.asList("aaa", "bbb", "ccc");
    private final List<String> list2 = Arrays.asList("aaa", "bbb", null);

    @Test
    public void onJoin() {
        String result = Joiner.on("#").join(list1);
        // aaa#bbb#ccc
        log.info("result={}", result);
    }

    @Test
    public void onJoinSkipNull() {
        String result = Joiner.on("#").skipNulls().join(list2);
        // aaa#bbb
        log.info("result={}", result);
    }

    @Test
    public void onJoinUseForNull() {
        String result = Joiner.on("#").useForNull("default").join(list2);
        // aaa#bbb#default
        log.info("result={}", result);
    }

    @Test
    public void onAppendTo() {
        StringBuilder result = Joiner.on("#").appendTo(new StringBuilder(), list1);
        // aaa#bbb#ccc
        log.info("result={}", result);
    }

    @Test
    public void streamSkipNull() {
        String result = list2.stream()
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.joining("#"));
        // aaa#bbb
        log.info("result={}", result);
    }

    @Test
    public void streamDefault() {
        String result = list2.stream()
                .map(item -> StrUtil.isBlank(item) ? "default" : item)
                .collect(Collectors.joining("#"));
        // aaa#bbb#default
        log.info("result={}", result);
    }
}

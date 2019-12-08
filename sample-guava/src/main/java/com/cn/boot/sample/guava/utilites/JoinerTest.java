package com.cn.boot.sample.guava.utilites;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
public class JoinerTest {

    private final List list1 = Arrays.asList("aaa", "bbb", "ccc");
    private final List list2 = Arrays.asList("aaa", "bbb", null);

    @Test
    public void test1() {
        String result1 = Joiner.on("#").join(list1);
        log.info("result1={}", result1);
    }
}

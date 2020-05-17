package com.cn.boot.sample.guava.utilities;

import cn.hutool.core.thread.ThreadUtil;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

/**
 * 用于统计耗时，可以自动转换单位
 *
 * @author Chen Nan
 */
@Slf4j
public class StopWatchTest {

    @Test
    public void test() {
        log.info("test start");
        Stopwatch stopwatch = Stopwatch.createStarted();

        ThreadUtil.sleep(RandomUtils.nextInt(100, 10000));

        log.info("test success, useTime={}", stopwatch.stop());
    }
}

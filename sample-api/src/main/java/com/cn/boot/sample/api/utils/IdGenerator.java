package com.cn.boot.sample.api.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @author Chen Nan
 */
public class IdGenerator {
    private static Snowflake snowflake = IdUtil.createSnowflake(1, 1);

    public static long nextId() {
        return snowflake.nextId();
    }
}

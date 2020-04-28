package com.cn.boot.sample.guava.cache;

import com.cn.boot.sample.api.model.po.User;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Slf4j
public class CacheTest {

    /**
     * 测试cache中用相同key取到的是否是同一个对象
     * 测试结果：是user1和user2的内存地址相同
     */
    @Test
    public void test() {
        int max = 5;
        Cache<String, User> users = CacheBuilder.newBuilder().maximumSize(max).
                expireAfterWrite(7, TimeUnit.DAYS).build();

        for (int i = 0; i < max + 3; i++) {
            String key = "user" + i;
            users.put(key, new User().setUsername(key));
            users.getIfPresent("user1");
        }
        log.info(users.asMap().values().toString());
    }
}

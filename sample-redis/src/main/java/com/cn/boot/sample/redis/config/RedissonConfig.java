package com.cn.boot.sample.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBloomFilter;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Configuration
public class RedissonConfig {

    @Bean
    public Redisson redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379").setDatabase(0);
        // 看门狗模式下锁过期时长，默认30s
        config.setLockWatchdogTimeout(30000);
        return (Redisson) Redisson.create(config);
    }

    @Bean
    public RAtomicLong atomicLong(Redisson redisson) {
        RAtomicLong atomicLong = redisson.getAtomicLong("testAtomicLong");
        return atomicLong;
    }

    @Bean
    public RBloomFilter<Long> bloomFilter(Redisson redisson) {
        RBloomFilter<Long> bloomFilter = redisson.getBloomFilter("bf:order");
        // 预计插入的数量
        long expectedInsertions = 1000000;
        // 误判率
        double falseProbability = 0.01;
        bloomFilter.tryInit(expectedInsertions, falseProbability);
        return bloomFilter;
    }
}

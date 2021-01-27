package com.cn.boot.sample.redis.config;

import org.redisson.Redisson;
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
}

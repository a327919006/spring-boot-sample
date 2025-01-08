package com.cn.boot.sample.redis.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Chen Nan
 */
@Slf4j
@Service
public class HyperLogLogServiceImpl {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 添加当日UV数
     *
     * @param userId 用户ID
     */
    public void add(Long userId) {
        // 1.拼接key
        String day = DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT);
        String key = "uv:" + day;
        // 3.写入Redis PFADD key value,注：支持批量写入
        stringRedisTemplate.opsForHyperLogLog().add(key, userId.toString());
    }

    /**
     * 获取当日UV
     *
     * @return UV数
     */
    public Long count() {
        // 1.拼接key
        String day = DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT);
        String key = "uv:" + day;
        // 2.获取数量 PFCOUNT key
        return stringRedisTemplate.opsForHyperLogLog().size(key);
    }
}

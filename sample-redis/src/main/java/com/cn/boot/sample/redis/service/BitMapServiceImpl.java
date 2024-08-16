package com.cn.boot.sample.redis.service;

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
public class BitMapServiceImpl {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户签到 SETBIT sign:用户id:当前月 当前月的第几天 1
     * SETBIT sign:1:202408 16 1
     *
     * @param userId 用户ID
     */
    public void sign(Long userId) {
        Date date = new Date();
        String month = DateUtil.format(date, "yyyyMM");
        String key = "sign:" + userId + ":" + month;
        int dayOfMonth = DateUtil.dayOfMonth(date);
        // 注：支持批量写入
        stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
    }

}

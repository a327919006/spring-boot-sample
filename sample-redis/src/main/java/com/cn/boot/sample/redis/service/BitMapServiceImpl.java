package com.cn.boot.sample.redis.service;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
        // 1.获取日期
        Date date = new Date();
        // 2.拼接key
        String month = DateUtil.format(date, "yyyyMM");
        String key = "sign:" + userId + ":" + month;
        // 3.获取今天是本月的第几天
        int dayOfMonth = DateUtil.dayOfMonth(date);
        // 4.写入Redis SETBIT key offset 1,注：支持批量写入
        stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
    }

    /**
     * 统计用户连续签到天数
     *
     * @param userId 用户ID
     * @return 连续签到天数
     */
    public int count(Long userId) {
        // 1.获取日期
        Date date = new Date();
        // 2.拼接key
        String month = DateUtil.format(date, "yyyyMM");
        String key = "sign:" + userId + ":" + month;
        // 3.获取今天是本月的第几天
        int dayOfMonth = DateUtil.dayOfMonth(date);
        List<Long> result = stringRedisTemplate.opsForValue().bitField(key,
                BitFieldSubCommands.create()
                        .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0));
        if (result == null || result.isEmpty()) {
            // 无签到信息
            return 0;
        }

        Long num = result.get(0);
        if (num == null || num == 0) {
            // 签到
            return 0;
        }

        int count = 0;
        // 如果为0，说明未签到，结束
        while ((num & 1) != 0) {
            // 让这个数字与1做与运算，得到数字的最后一个bit位  // 判断这个bit位是否为0
            // 如果不为0，说明已签到，计数器+1
            count++;
            // 把数字右移一位，抛弃最后一个bit位，继续下一个bit位
            num = num >> 1;
        }
        return count;
    }
}

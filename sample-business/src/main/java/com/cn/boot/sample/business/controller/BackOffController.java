package com.cn.boot.sample.business.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.cn.boot.sample.api.model.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.BackOffExecution;
import org.springframework.util.backoff.ExponentialBackOff;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/backoff")
@Api(tags = "测试退避", produces = MediaType.APPLICATION_JSON_VALUE)
public class BackOffController {

    /**
     * 固定间隔退避
     *
     * @param interval 间隔时间
     * @param max      最大次数
     */
    @ApiOperation("FixedBackOff")
    @GetMapping("/fixed")
    public String fixedBackOff(long interval, int max) {
        BackOff backOff = new FixedBackOff(interval, max);
        BackOffExecution execution = backOff.start();

        for (int i = 0; i <= max; i++) {
            long value = execution.nextBackOff();
            log.info("value={}", value);

            if (value == BackOffExecution.STOP) {
                break;
            }
            // 执行相关业务
            log.info("test backoff={}", i);
            boolean result = false;
            // 如果业务执行成功，则退出
            if (result) {
                break;
            }
            // 业务执行失败，退避重试
            ThreadUtil.sleep(value);
        }

        return Constants.MSG_SUCCESS;
    }

    /**
     * 指数间隔退避
     *
     * @param interval   间隔时间
     * @param multiplier 倍数
     * @param max        最大次数
     */
    @ApiOperation("ExponentialBackOff")
    @GetMapping("/exponential")
    public String exponentialBackOff(long interval, int multiplier, int max) {
        BackOff backOff = new ExponentialBackOff(interval, multiplier);
        BackOffExecution execution = backOff.start();

        for (int i = 0; i < max; i++) {
            long value = execution.nextBackOff();
            log.info("value={}", value);

            if (value == BackOffExecution.STOP) {
                break;
            }
            // 执行相关业务
            log.info("test backoff={}", i);
            boolean result = false;
            // 如果业务执行成功，则退出
            if (result) {
                break;
            }
            // 业务执行失败，退避重试
            ThreadUtil.sleep(value);
        }

        return Constants.MSG_SUCCESS;
    }
}

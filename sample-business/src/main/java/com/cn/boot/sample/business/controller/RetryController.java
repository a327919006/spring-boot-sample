package com.cn.boot.sample.business.controller;

import cn.hutool.http.HttpRequest;
import com.cn.boot.sample.api.model.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/retry")
@Api(tags = "重试", produces = MediaType.APPLICATION_JSON_VALUE)
public class RetryController {

    /**
     * SimpleRetryPolicy
     * 设置最大重试次数
     */
    @ApiOperation("SimpleRetryPolicy")
    @GetMapping("/simple")
    public String simpleRetryPolicy(int maxAttemptes) throws Exception {
        RetryTemplate template = new RetryTemplate();

        // 超时重试策略
        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        // 超时时间 默认 1000 毫秒
        policy.setMaxAttempts(maxAttemptes);

        // 设置重试策略
        template.setRetryPolicy(policy);

        // 执行
        String result = template.execute(
                (RetryCallback<String, Exception>) context -> {
                    doBusiness();
                    return Constants.MSG_SUCCESS;
                },
                // 当重试执行完闭，操作还未成为，那么可以通过RecoveryCallback完成一些失败事后处理。
                context -> {
                    log.error("经过重试，还是失败，返回错误");
                    return Constants.MSG_FAIL;
                }
        );

        return result;
    }

    /**
     * TimeoutRetryPolicy
     * 设置最长重试时长,会在此时长内不限次数，一直重试
     * 举例：http请求外部接口，接口超时时间10ms，重试策略超时时长100ms，如果接口在10ms未返回结果，则会在100毫秒内一直重试。
     */
    @ApiOperation("TimeoutRetryPolicy")
    @GetMapping("/timeout")
    public String timeoutRetryPolicy(long timeout) throws Exception {
        RetryTemplate template = new RetryTemplate();

        // 超时重试策略
        TimeoutRetryPolicy policy = new TimeoutRetryPolicy();
        // 超时时间 默认 1000 毫秒
        policy.setTimeout(timeout);

        // 设置重试策略
        template.setRetryPolicy(policy);

        // 执行
        String result = template.execute(
                (RetryCallback<String, Exception>) context -> {
                    doBusiness();
                    return Constants.MSG_SUCCESS;
                },
                context -> {
                    log.error("经过重试，还是失败，返回错误");
                    return Constants.MSG_FAIL;
                }
        );

        return result;
    }

    private void doBusiness() {
        log.info("执行业务");
        String result = HttpRequest.get("https://www.baidu.com/").timeout(10).execute().body();
        log.info("result={}", result);
    }
}

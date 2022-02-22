package com.cn.boot.sample.business.controller;

import cn.hutool.http.HttpRequest;
import com.cn.boot.sample.api.model.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.ExponentialRandomBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.backoff.UniformRandomBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 参考博客：https://blog.csdn.net/easy_to_know/article/details/86611839
 * <p>
 * 简介：
 * <p>
 * Spring-retry提供的RetryOperations接口，该接口提供了若干方法来执行重试操作
 * <p>
 * public interface RetryOperations {
 * <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E;
 * <p>
 * <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback, RecoveryCallback<T> recoveryCallback) throws E;
 * <p>
 * <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback, RetryState retryState) throws E, ExhaustedRetryException;
 * <p>
 * <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback, RecoveryCallback<T> recoveryCallback, RetryState retryState) throws E;
 * }
 * <p>
 * 调用者通过传入RetryCallback来完成调用者的重试操作。如果callback执行失败(抛出某些异常)，那么会按照调用者设定的策略进行重试。
 * 重试操作直到成功，或根据使用者设定的条件而退出。
 * <p>
 * RetryCallback的接口定义如下：
 * <p>
 * public interface RetryCallback<T, E extends Throwable> {
 * <p>
 * T doWithRetry(RetryContext context) throws E;
 * }
 *
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/retry")
@Api(tags = "测试14-重试", produces = MediaType.APPLICATION_JSON_VALUE)
public class RetryController {

    /**
     * SimpleRetryPolicy 策略
     * <p>
     * will execute the callback at least once, and as many as 3 times.
     * 该策略定义了对指定的异常进行若干次重试。默认情况下，对Exception异常及其子类重试3次.
     * 如果创建SimpleRetryPolicy并指定重试异常map，可以选择性重试或不进行重试.
     */
    @ApiOperation("SimpleRetryPolicy-简单重试")
    @GetMapping("/simple")
    public String simpleRetryPolicy(int maxAttemptes) throws Exception {
        RetryTemplate template = new RetryTemplate();

        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        // 设置重试次数
        policy.setMaxAttempts(maxAttemptes);

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
    @ApiOperation("TimeoutRetryPolicy-超时重试")
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


    /**
     * 退避(BackOff)策略
     * <p>
     * 当操作执行失败时，根据设置的重试策略进行重试。通过BackoffPolicy可以设定再次重试的时间间隔。
     * <p>
     * 接口：
     * public interface BackOffPolicy {
     * BackOffContext start(RetryContext context);
     * void backOff(BackOffContext backOffContext) throws BackOffInterruptedException;
     * }
     * <p>
     * BackOff策略接口的具体实现:
     * <p>
     * interface BackOffPolicy
     * // 实现BackOff接口的抽象类
     * abstract StatelessBackOffPolicy
     * class FixedBackOffPolicy // 在等待一段固定的时间后，再进行重试。默认为1秒。
     * class NoBackOffPolicy    // 实现了空方法，因此采用次策略，重试不会等待。这也是RetryTemplate采用的默认退避(backOff)策略
     * class UniformRandomBackOffPolicy // 均匀随机退避策略，等待时间为 最小退避时间 + [0,最大退避时间 - 最小退避时间)间的一个随机数，如果最大退避时间等于最小退避时间那么等待时间为0。
     * // 继承BackOff接口的接口
     * interface SleepingBackOffPolicy
     * class ExponentialBackOffPolicy // 指数退避策略 ，每次等待时间为 等待时间 = 等待时间 * N ，即每次等待时间为上一次的N倍。如果等待时间超过最大等待时间，那么以后的等待时间为最大等待时间。
     * // 该类是ExponentialBackOffPolicy的子类
     * class ExponentialRandomBackOffPolicy // 指数随机策略
     * class FixedBackOffPolicy    // 与StatelessBackoffPolicy的同名实现类返回等待时间的方法是一致的。而两者的主要区别是，SleepingbackOffPolicy可以设置用户定义的Sleeper。
     * class UniformRandomBackOffPolicy // 与StatelessBackoffPolicy的同名实现类返回等待时间的方法是一致的。而两者的主要区别是，SleepingbackOffPolicy可以设置用户定义的Sleeper。
     */
    @ApiOperation("FixedBackOffPolicy-固定时间间隔退避重试")
    @GetMapping("/fixedBackOff")
    public String fixedBackOffPolicy(int maxAttempts, long backOffPeriod) throws Exception {
        RetryTemplate template = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempts);
        // 设置重试策略
        template.setRetryPolicy(retryPolicy);

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        // 设置退避时间间隔
        backOffPolicy.setBackOffPeriod(backOffPeriod);
        // 设置退避策略
        template.setBackOffPolicy(backOffPolicy);

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

    @ApiOperation("ExponentialBackOffPolicy-指数退避重试")
    @GetMapping("/exponentialBackOffPolicy")
    public String exponentialBackOffPolicy(int maxAttempts, long initialInterval,
                                           long multiplier, int maxInterval) throws Exception {
        RetryTemplate template = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempts);
        // 设置重试策略
        template.setRetryPolicy(retryPolicy);

        // 每次等待时间为 等待时间 = 等待时间 * N ，即每次等待时间为上一次的N倍。 (如果等待时间超过最大等待时间，那么以后的等待时间为最大等待时间。)
        // 以下设置 初始时间间隔为2000毫秒，N = 3，¸最大间隔为6000毫秒，那么从第3次重试开始，以后每次等待时间都为6000毫秒。
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(initialInterval);
        backOffPolicy.setMultiplier(multiplier);
        backOffPolicy.setMaxInterval(maxInterval);
        // 设置退避策略
        template.setBackOffPolicy(backOffPolicy);

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

    @ApiOperation("ExponentialRandomBackOffPolicy-指数随机退避重试")
    @GetMapping("/exponentialRandomBackOffPolicy")
    public String exponentialRandomBackOffPolicy(int maxAttempts, long initialInterval,
                                                 long multiplier, int maxInterval) throws Exception {
        RetryTemplate template = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempts);
        // 设置重试策略
        template.setRetryPolicy(retryPolicy);

        // 算法是 等待时间 = 等待时间 * (1 + Random（随机数） * (N - 1))
        ExponentialRandomBackOffPolicy backOffPolicy = new ExponentialRandomBackOffPolicy();
        backOffPolicy.setInitialInterval(initialInterval);
        backOffPolicy.setMultiplier(multiplier);
        backOffPolicy.setMaxInterval(maxInterval);
        // 设置退避策略
        template.setBackOffPolicy(backOffPolicy);

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

    @ApiOperation("UniformRandomBackOffPolicy -均匀随机退避重试")
    @GetMapping("/uniformRandomBackOffPolicy ")
    public String exponentialRandomBackOffPolicy(int maxAttempts, long minBackOffPeriod,
                                                 long maxBackOffPeriod) throws Exception {
        RetryTemplate template = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(maxAttempts);
        // 设置重试策略
        template.setRetryPolicy(retryPolicy);

        // 等待时间为 最小退避时间 + [0, 最大退避时间 - 最小退避时间)间的一个随机数 (如果最大退避时间等于最小退避时间那么等待时间为0)
        UniformRandomBackOffPolicy backOffPolicy = new UniformRandomBackOffPolicy();
        backOffPolicy.setMinBackOffPeriod(minBackOffPeriod);
        backOffPolicy.setMaxBackOffPeriod(maxBackOffPeriod);
        // 设置退避策略
        template.setBackOffPolicy(backOffPolicy);

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


    @ApiOperation("Listener-监听器")
    @GetMapping("/listener")
    public String listen(int maxAttemptes) throws Exception {
        RetryTemplate template = new RetryTemplate();

        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(maxAttemptes);
        // 设置重试策略
        template.setRetryPolicy(policy);


        // 定义监听器1
        RetryListener retryListener1 = new RetryListener() {

            @Override
            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                log.info("1-open");
                return true;
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
                                                         Throwable throwable) {
                log.info("1-onError");
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
                                                       Throwable throwable) {
                log.info("1-close");
            }
        };

        // 定义监听器2
        RetryListener retryListener2 = new RetryListener() {

            @Override
            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                log.info("2-open");
                return true;
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
                                                         Throwable throwable) {
                log.info("2-onError");
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
                                                       Throwable throwable) {
                log.info("2-close");
            }
        };

        RetryListener[] retryListeners = {retryListener1, retryListener2};
        // 设置监听器
        template.setListeners(retryListeners);

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
     * 模拟业务执行
     */
    private void doBusiness() {
        log.info("执行业务");
        String result = HttpRequest.get("https://www.baidu.com/").timeout(10).execute().body();
        log.info("result={}", result);
    }
}

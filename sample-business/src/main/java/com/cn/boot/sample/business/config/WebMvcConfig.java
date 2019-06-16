package com.cn.boot.sample.business.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Callable;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2018/10/8.
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ThreadPoolTaskExecutor poolTaskExecutor;

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // 设置异步接口拦截器，Callable方式
        configurer.registerCallableInterceptors(new CallableProcessingInterceptor() {
            @Override
            public <T> void beforeConcurrentHandling(NativeWebRequest request, Callable<T> task) throws Exception {
                log.info("Callable-beforeConcurrentHandling");
            }
        });

        // 设置异步接口拦截器，DeferredResult方式
        configurer.registerDeferredResultInterceptors(new DeferredResultProcessingInterceptor() {
            @Override
            public <T> void beforeConcurrentHandling(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {
                log.info("DeferredResult-beforeConcurrentHandling");
            }
        });
        // 设置异步接口超时时间
        configurer.setDefaultTimeout(30000);

        // 设置异步任务线程池
        configurer.setTaskExecutor(poolTaskExecutor);
    }
}

package com.cn.boot.sample.business.config;

import com.cn.boot.sample.business.aop.CheckInterceptor;
import com.cn.boot.sample.business.aop.DataArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * WebMvc相关配置
 *
 * @author Chen Nan
 * @date 2018/10/8.
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ThreadPoolTaskExecutor poolTaskExecutor;

    @Autowired
    private DataArgumentResolver dataArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new CheckInterceptor());
        registration.addPathPatterns("/**")                      //所有路径都被拦截
                .excludePathPatterns(                         //添加不拦截路径
                        "/**/*.html",
                        "/**/*.js"
                )
                .order(1); // 执行顺序
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 配置自定义参数处理器
//        resolvers.add(dataArgumentResolver);
    }

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

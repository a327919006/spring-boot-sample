package com.cn.boot.sample.server.mp.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chen Nan
 */
@Configuration
public class MyBatisConfig {

    @Autowired
    private CustomTenantHandler customTenantHandler;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 单页分页条数限制
        paginationInnerInterceptor.setMaxLimit(100L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        //多租户插件
        // TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor(customTenantHandler);
        // interceptor.addInnerInterceptor(tenantInterceptor);
        return interceptor;
    }

    @Bean
    public CustomSqlInjector sqlInjector() {
        return new CustomSqlInjector();
    }
}

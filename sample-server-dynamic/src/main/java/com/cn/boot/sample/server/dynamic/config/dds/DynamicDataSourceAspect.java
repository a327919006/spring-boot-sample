package com.cn.boot.sample.server.dynamic.config.dds;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 动态数据源切换处理器
 *
 * @author Louis
 * @date Jun 17, 2019
 */
@Slf4j
@Aspect
@Order(-1)  // 该切面应当先于 @Transactional 执行
@Component
public class DynamicDataSourceAspect {

    /**
     * 切换数据源
     *
     * @param point
     * @param dataSource
     */
    @Before("@annotation(dataSource))")
    public void switchDataSource(JoinPoint point, DS dataSource) {
        Object arg = point.getArgs()[0];
        if (arg instanceof String) {
            String appId = (String) point.getArgs()[0];
            if (!DynamicDataSourceContextHolder.containDataSourceKey("ds"+appId)) {
                log.info("switchDataSource [{}] doesn't exist, use default DataSource [{}] " + appId, DynamicDataSourceContextHolder.getDataSourceKey());
            } else {
                // 切换数据源
                DynamicDataSourceContextHolder.setDataSourceKey("ds"+appId);
                log.info("switchDataSource [{}] in Method [{}]", DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
            }
        } else {
            log.error("switchDataSource missing data source key");
        }
    }

    /**
     * 重置数据源
     *
     * @param point
     * @param dataSource
     */
    @After("@annotation(dataSource))")
    public void restoreDataSource(JoinPoint point, DS dataSource) {
        // 将数据源置为默认数据源
        DynamicDataSourceContextHolder.clearDataSourceKey();
        log.info("restoreDataSource to [{}] in Method [{}]", DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());

    }
}
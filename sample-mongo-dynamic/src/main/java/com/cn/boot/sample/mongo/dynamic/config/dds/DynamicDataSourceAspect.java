package com.cn.boot.sample.mongo.dynamic.config.dds;

import com.cn.boot.sample.mongo.dynamic.util.MongoTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 动态数据源切换处理器
 *
 * @author ChenNan
 */
@Slf4j
@Aspect
@Order(-1)  // 该切面应当先于 @Transactional 执行
@Component
public class DynamicDataSourceAspect {

    @Autowired
    private MongoTemplateUtil mongoTemplateUtil;

    /**
     * 切换数据源
     */
    @Before("@annotation(dataSource))")
    public void switchDataSource(JoinPoint point, DynamicDataSource dataSource) {
        Object arg = point.getArgs()[0];
        if (arg instanceof String) {
            String appId = (String) point.getArgs()[0];
            String key = "ds" + appId;

            if (!mongoTemplateUtil.isExistDataSource(key)) {
                log.info("switchDataSource [{}] doesn't exist, use default", key);
            } else {
                // 切换数据源
                DynamicDataSourceContextHolder.setDataSourceKey(key);
                log.info("switchDataSource [{}] in Method [{}]", DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
            }
        } else {
            log.error("switchDataSource missing data source key");
        }
    }
}
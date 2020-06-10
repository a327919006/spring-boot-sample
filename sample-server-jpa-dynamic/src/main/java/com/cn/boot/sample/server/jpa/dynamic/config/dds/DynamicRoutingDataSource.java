package com.cn.boot.sample.server.jpa.dynamic.config.dds;

import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源实现类
 *
 * @author ChenNan
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private static Map<Object, Object> targetDataSources = new HashMap<>();

    /**
     * 如果希望所有数据源在启动配置时就加载好，这里通过设置数据源Key值来切换数据，定制这个方法
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }

    /**
     * 是否存在当前key的 DataSource
     *
     * @return 存在返回 true, 不存在返回 false
     */
    public static boolean isExistDataSource(String key) {
        return targetDataSources.containsKey(key);
    }

    /**
     * 设置数据源
     */
    public void setDataSources(Map<Object, Object> dataSources) {
        super.setTargetDataSources(dataSources);
        DynamicRoutingDataSource.targetDataSources = dataSources;
    }

    /**
     * 初始化缓存配置
     */
    public void initConfig(String propertyValue) {
        JSONObject config = JSONObject.parseObject(propertyValue);
        config.keySet().forEach(appId -> {
            Map<String, String> dbConfig = JSONObject.parseObject(config.getString(appId), Map.class);
            String key = "ds" + appId;
            addDataSource(key, dbConfig);
        });
    }

    /**
     * 创建数据源
     */
    public HikariDataSource createDataSource(Map<String, String> dbConfig) {
        String driver = dbConfig.get("driver");
        String url = dbConfig.get("url");
        String username = dbConfig.get("username");
        String password = dbConfig.get("password");
        DataSourceBuilder<HikariDataSource> hikariDataSourceBuilder = DataSourceBuilder.create().type(HikariDataSource.class);
        HikariDataSource hikariDataSource = hikariDataSourceBuilder
                .driverClassName(driver)
                .url(url)
                .username(username)
                .password(password)
                .build();

        //配置Hikari连接池
        hikariDataSource.setAutoCommit(true);//update自动提交设置
        hikariDataSource.setConnectionTestQuery("select 1");//连接查询语句设置
        hikariDataSource.setConnectionTimeout(3000);//连接超时时间设置
        hikariDataSource.setIdleTimeout(3000);//连接空闲生命周期设置
        hikariDataSource.setIsolateInternalQueries(false);//执行查询启动设置
        hikariDataSource.setMaximumPoolSize(3000);//连接池允许的最大连接数量
        hikariDataSource.setMaxLifetime(1800000);//检查空余连接优化连接池设置时间,单位毫秒
        hikariDataSource.setMinimumIdle(10);//连接池保持最小空余连接数量
        hikariDataSource.setPoolName("hikariPool");//连接池名称

        return hikariDataSource;
    }

    /**
     * 动态增加数据源
     *
     * @param dbKey    数据源key
     * @param dbConfig 数据源信息
     */
    public synchronized boolean addDataSource(String dbKey, Map<String, String> dbConfig) {
        try {
            if (StringUtils.isBlank(dbKey)) {
                log.error("addDataSource missing config database name");
                return false;
            }
            if (DynamicRoutingDataSource.isExistDataSource(dbKey)) {
                return true;
            }
            HikariDataSource dataSource = createDataSource(dbConfig);

            Map<Object, Object> targetMap = DynamicRoutingDataSource.targetDataSources;
            targetMap.put(dbKey, dataSource);
            this.afterPropertiesSet();
            log.info("addDataSource {} has been added", dbKey);
        } catch (Exception e) {
            log.error("addDataSource error", e);
            return false;
        }
        return true;
    }
}

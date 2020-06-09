package com.cn.boot.sample.server.dynamic.config.dds;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
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
     * 如果不希望数据源在启动配置时就加载好，可以定制这个方法，从任何你希望的地方读取并返回数据源
     * 比如从数据库、文件、外部接口等读取数据源信息，并最终返回一个DataSource实现类对象即可
     */
    @Override
    protected DataSource determineTargetDataSource() {
        return super.determineTargetDataSource();
    }

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
     * 设置默认数据源
     */
    public void setDefaultDataSource(Object defaultDataSource) {
        super.setDefaultTargetDataSource(defaultDataSource);
    }

    /**
     * 设置数据源
     */
    public void setDataSources(Map<Object, Object> dataSources) {
        super.setTargetDataSources(dataSources);
        DynamicRoutingDataSource.targetDataSources = dataSources;
    }

    /**
     * 动态增加数据源
     *
     * @param map 数据源属性
     */
    public synchronized boolean addDataSource(Map<String, String> map) {
        try {
            Connection connection = null;
            // 排除连接不上的错误
            try {
                Class.forName(map.get(DruidDataSourceFactory.PROP_DRIVERCLASSNAME));
                connection = DriverManager.getConnection(
                        map.get(DruidDataSourceFactory.PROP_URL),
                        map.get(DruidDataSourceFactory.PROP_USERNAME),
                        map.get(DruidDataSourceFactory.PROP_PASSWORD));
                log.info("addDataSource connectionIsClose={}", connection.isClosed());
            } catch (Exception e) {
                log.error("addDataSource connect error", e);
                return false;
            } finally {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
            String database = map.get("database");//获取要添加的数据库名
            if (StringUtils.isBlank(database)) {
                return false;
            }
            if (DynamicRoutingDataSource.isExistDataSource(database)) {
                return true;
            }
            DruidDataSource druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(map);
            druidDataSource.init();
            Map<Object, Object> targetMap = DynamicRoutingDataSource.targetDataSources;
            targetMap.put(database, druidDataSource);
            // 当前 targetDataSources 与 父类 targetDataSources 为同一对象 所以不需要set
//          this.setTargetDataSources(targetMap);
            this.afterPropertiesSet();
            log.info("addDataSource {} has been added", database);
        } catch (Exception e) {
            log.error("addDataSource error", e);
            return false;
        }
        return true;
    }
}
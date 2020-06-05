package com.cn.boot.sample.server.dynamic.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.cn.boot.sample.server.dynamic.config.dds.DynamicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Chen Nan
 */
@Configuration
@MapperScan(basePackages = {"com.cn.boot.sample.dal.mapper"})
public class DynamicDataSourceConfig {

    @Value("${mybatis.typeAliasesPackage}")
    private String typeAliasesPackage;
    @Value("${mybatis.mapperLocations}")
    private String mapperLocations;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.db1")
    @Primary
    public DataSource db1DataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.db2")
    public DataSource db2DataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(2);
        dataSourceMap.put("ds100010020", db1DataSource());
        dataSourceMap.put("ds100010021", db2DataSource());
        // 将 master 数据源作为默认指定的数据源
        dynamicDataSource.setDefaultDataSource(db1DataSource());
        // 将 master 和 slave 数据源作为指定的数据源
        dynamicDataSource.setDataSources(dataSourceMap);
        return dynamicDataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource());
        bean.setTypeAliasesPackage(typeAliasesPackage);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        tk.mybatis.mapper.session.Configuration configuration = new tk.mybatis.mapper.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        Properties properties = new Properties();
        properties.setProperty("style", "normal");
        configuration.setMapperProperties(properties);
        bean.setConfiguration(configuration);
        return bean.getObject();
    }
}

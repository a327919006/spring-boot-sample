package com.cn.boot.sample.server.dynamic.service;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.cn.boot.sample.api.service.DynamicDataSourceService;
import com.cn.boot.sample.server.dynamic.config.dds.DynamicRoutingDataSource;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Service
public class DynamicDataSourceServiceImpl implements DynamicDataSourceService {

    @Autowired
    private DynamicRoutingDataSource dynamicDataSource;

    @Override
    public void addDataSource(String key, String url, String username, String password) {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("database", key);
        configMap.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, "com.mysql.jdbc.Driver");
        configMap.put(DruidDataSourceFactory.PROP_URL, url);
        configMap.put(DruidDataSourceFactory.PROP_USERNAME, username);
        configMap.put(DruidDataSourceFactory.PROP_PASSWORD, password);
        dynamicDataSource.addDataSource(configMap);
    }
}

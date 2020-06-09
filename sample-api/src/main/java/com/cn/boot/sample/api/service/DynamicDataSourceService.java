package com.cn.boot.sample.api.service;

/**
 * @author Chen Nan
 */
public interface DynamicDataSourceService {
    void addDataSource(String key, String url, String username, String password);

}

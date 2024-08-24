package com.cn.boot.sample.api.service;

import com.cn.boot.sample.api.model.po.Client;

/**
 * redis缓存服务
 *
 * @author Chen Nan
 */
public interface CacheService {

    /**
     * 缓存对象
     *
     * @param key    key
     * @param object 数据
     */
    void cache(String key, Object object);

    /**
     * 获取对象
     *
     * @param key key
     * @return 对象
     */
    Object get(String key);

    /**
     * 获取商户
     *
     * @param id 商户ID
     * @return 商户信息
     */
    Client getClient(String id);
}

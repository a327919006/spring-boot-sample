package com.cn.boot.sample.api.service;

/**
 * @author Chen Nan
 */
public interface RedisService {

    /**
     * 添加分布式锁
     *
     * @param key   key
     * @param value value
     */
    void lock(String key, String value);

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
}

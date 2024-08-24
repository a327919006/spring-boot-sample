package com.cn.boot.sample.redis.service;

import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.CacheService;
import com.cn.boot.sample.api.service.ClientService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author Chen Nan
 */
@Service(timeout = 300000)
@CacheConfig(cacheNames = "CLIENT")
public class CacheServiceImpl implements CacheService {

    @Reference
    private ClientService clientService;

    private ValueOperations valueOperations;

    @Autowired
    public CacheServiceImpl(RedisTemplate redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public void cache(String key, Object object) {
        valueOperations.set(key, object);
    }

    @Override
    public Object get(String key) {
        return valueOperations.get(key);
    }

    @Override
    @Cacheable(key = "'client_'+#id")
    public Client getClient(String id) {
        return clientService.selectByPrimaryKey(id);
    }
}

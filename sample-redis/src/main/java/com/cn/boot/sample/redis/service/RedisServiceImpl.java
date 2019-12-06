package com.cn.boot.sample.redis.service;

import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.ClientService;
import com.cn.boot.sample.api.service.RedisService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Service(timeout = 300000)
@CacheConfig(cacheNames = "CLIENT")
public class RedisServiceImpl implements RedisService {

    @Reference
    private ClientService clientService;

    private RedisTemplate redisTemplate;
    private HashOperations hashOperations;
    private ListOperations listOperations;
    private ZSetOperations zSetOperations;
    private SetOperations setOperations;
    private ValueOperations valueOperations;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.listOperations = redisTemplate.opsForList();
        this.zSetOperations = redisTemplate.opsForZSet();
        this.setOperations = redisTemplate.opsForSet();
        this.valueOperations = redisTemplate.opsForValue();
    }


    @Override
    public boolean lock(String key, String value) {
        return valueOperations.setIfAbsent(key, value, 30, TimeUnit.SECONDS);
    }

    @Override
    public boolean unlock(String key) {
        return stringRedisTemplate.delete(key);
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

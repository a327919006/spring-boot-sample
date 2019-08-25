package com.cn.boot.sample.redis.service;

import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.ClientService;
import com.cn.boot.sample.api.service.RedisService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Service(timeout = 300000)
public class RedisServiceImpl implements RedisService {

    @Reference
    private ClientService clientService;

    private RedisTemplate redisTemplate;
    private StringRedisTemplate stringRedisTemplate;
    private HashOperations hashOperations;
    private ListOperations listOperations;
    private ZSetOperations zSetOperations;
    private SetOperations setOperations;
    private ValueOperations valueOperations;

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
    public void lock(String key, String value) {
        valueOperations.set(key, value, 30, TimeUnit.SECONDS);
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
    @Cacheable(key = "'client_'+#id", value = "CLIENT")
    public Client getClient(String id) {
        return clientService.selectByPrimaryKey(id);
    }
}

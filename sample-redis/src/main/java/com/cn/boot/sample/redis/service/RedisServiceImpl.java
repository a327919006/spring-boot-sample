package com.cn.boot.sample.redis.service;

import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.ClientService;
import com.cn.boot.sample.api.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
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
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, 30, TimeUnit.SECONDS);
    }

    @Override
    public boolean unlock(String key, String value) {
        // 直接del可能删除别人的锁，因此要先get判断线程标识再删除

        // 这样写有线程安全问题
        // String lockValue = stringRedisTemplate.opsForValue().get(key);
        // if(StringUtils.equals(value, lockValue)){
        //     stringRedisTemplate.delete(key);
        // }
        // return true;

        // 使用lua脚本，原子性操作get与del，防止线程安全问题
        String script =
                "if (redis.call('GET', KEYS[1]) == ARGV[1]) then\n" +
                "  return redis.call('DEL', KEYS[1])\n" +
                "end\n" +
                "return 0";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        long result = stringRedisTemplate.execute(redisScript, Collections.singletonList(key), value);
        return 1L == result;
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

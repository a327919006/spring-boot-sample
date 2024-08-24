package com.cn.boot.sample.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanResult;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Service
public class RedisServiceImpl {

    /**
     * BigKey判断标准
     */
    private final static int STR_MAX_LEN = 10 * 1024;
    private final static int HASH_MAX_LEN = 500;

    private RedisTemplate redisTemplate;
    private HashOperations hashOperations;
    private ListOperations listOperations;
    private ZSetOperations zSetOperations;
    private SetOperations setOperations;
    private ValueOperations valueOperations;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private JedisPool jedisPool;

    @Autowired
    public RedisServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.listOperations = redisTemplate.opsForList();
        this.zSetOperations = redisTemplate.opsForZSet();
        this.setOperations = redisTemplate.opsForSet();
        this.valueOperations = redisTemplate.opsForValue();
    }

    /**
     * 添加分布式锁
     *
     * @param key   key
     * @param value value
     * @return 结果
     */
    public boolean lock(String key, String value) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, 30, TimeUnit.SECONDS);
    }

    /**
     * 移除分布式锁
     *
     * @param key   key
     * @param value value
     * @return 结果
     */
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

    /**
     * 使用scan命令遍历搜索BigKey
     * 判断依据：
     * 1、string类型的数据长度不超过10k
     * 2、hash、list、set、zset等类型的集合内子数量不超过500个
     */
    public void scanBigKey() {
        Jedis jedis = jedisPool.getResource();
        int maxLen = 0;
        long len = 0;

        String cursor = "0";
        do {
            // 扫描并获取一部分key
            ScanResult<String> result = jedis.scan(cursor);
            // 记录cursor
            cursor = result.getCursor();
            List<String> list = result.getResult();
            if (list == null || list.isEmpty()) {
                break;
            }
            // 遍历
            for (String key : list) {
                // 判断key的类型
                String type = jedis.type(key);
                switch (type) {
                    case "string":
                        len = jedis.strlen(key);
                        maxLen = STR_MAX_LEN;
                        break;
                    case "hash":
                        len = jedis.hlen(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    case "list":
                        len = jedis.llen(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    case "set":
                        len = jedis.scard(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    case "zset":
                        len = jedis.zcard(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    default:
                        break;
                }
                if (len >= maxLen) {
                    System.out.printf("Found big key : %s, type: %s, length or size: %d %n", key, type, len);
                }
            }
        } while (!"0".equals(cursor));
    }
}

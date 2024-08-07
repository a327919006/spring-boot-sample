package com.cn.boot.sample.redis.controller;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/common/test")
@Api(tags = "1、基础功能测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonTestController {

    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private RAtomicLong atomicLong;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${spring.redis.database}")
    private int database;

    @ApiOperation("1、hgetAll命令测试，默认返回空map")
    @PostMapping("/hgetAll")
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = jedisPool.getResource();
        return jedis.hgetAll(key);
    }

    @ApiOperation("2、测试redisson，AtomicLong")
    @GetMapping("/atomic/addAndGet")
    public String atomic(long expire) {
        String num = String.valueOf(atomicLong.addAndGet(1));
        atomicLong.expire(expire, TimeUnit.MILLISECONDS);
        return StrUtil.fillBefore(num, '0', 4);
    }

    @ApiOperation("3、lua脚本测试")
    @GetMapping("/lua")
    public String lua(Integer num) {
        Jedis jedis = jedisPool.getResource();
        String script = "local value = redis.call('get', KEYS[1])\n" +
                "if value == false then\n" +
                "    redis.call('set', KEYS[1], ARGV[1])\n" +
                "\treturn ARGV[1]\n" +
                "else\n" +
                "    return redis.call('incr', KEYS[1])\n" +
                "end\t";
        List<String> keys = Lists.newArrayList("hellolua");
        List<String> args = Lists.newArrayList(num.toString());
        return jedis.eval(script, keys, args).toString();
    }

    @ApiOperation("4、lua脚本测试-StringRedisTemplate")
    @GetMapping("/lua/stringRedisTemplate")
    public Long luaStringRedisTemplate(Long num) {
        String script = "local value = redis.call('get', KEYS[1])\n" +
                "if value == false then\n" +
                "    redis.call('set', KEYS[1], ARGV[1])\n" +
                "\treturn ARGV[1]\n" +
                "else\n" +
                "    return redis.call('incr', KEYS[1])\n" +
                "end\t";
        List<String> keys = Lists.newArrayList("hellolua");
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = stringRedisTemplate.execute(redisScript, keys, num.toString());
        if(result == null){
            return num;
        }
        return result;
    }
}

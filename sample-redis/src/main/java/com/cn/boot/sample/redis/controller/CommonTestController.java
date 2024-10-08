package com.cn.boot.sample.redis.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.redis.dto.ScrollResult;
import com.cn.boot.sample.redis.service.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.redis.connection.ClusterSlotHashUtil;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/common/test")
@Api(tags = "1、基础功能测试", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonTestController {

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ZSetServiceImpl scrollService;
    @Autowired
    private GeoServiceImpl geoService;
    @Autowired
    private BitMapServiceImpl bitMapService;
    @Autowired
    private HyperLogLogServiceImpl hyperLogLogService;
    @Autowired
    private RedisServiceImpl redisService;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    @Value("${spring.redis.database}")
    private int database;

    @ApiOperation("1、hgetAll命令测试，默认返回空map")
    @PostMapping("/hgetAll")
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = jedisPool.getResource();
        return jedis.hgetAll(key);
    }

    @ApiOperation("2、lua脚本测试-Jedis")
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

    @ApiOperation("3、lua脚本测试-StringRedisTemplate")
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
        if (result == null) {
            return num;
        }
        return result;
    }

    @ApiOperation("4、lua脚本-秒杀")
    @GetMapping("/lua/seckill")
    public Long seckill(String voucherId, String userId, String orderId) {
        return stringRedisTemplate.execute(SECKILL_SCRIPT,
                Collections.emptyList(), voucherId, userId, orderId);
    }

    @ApiOperation("5、zset实现滚动分页查询")
    @GetMapping("/scrollPage")
    public ScrollResult scrollPage(Long max, Integer offset) {
        return scrollService.scrollPage(max, offset);
    }

    @ApiOperation("6、GEO-add-添加地理坐标")
    @GetMapping("/geo/add")
    public Long geoAdd(String type, String shopId, double longitude, double latitude) {
        return geoService.add(type, shopId, longitude, latitude);
    }

    @ApiOperation("7、GEO-add-添加地理坐标")
    @GetMapping("/geo/search")
    public List<GeoResult<RedisGeoCommands.GeoLocation<String>>> gepSearch(String type, Double longitude, Double latitude,
                                                                           Double distance, Integer page) {
        if (page == null) {
            page = 1;
        }
        return geoService.searchPage(type, longitude, latitude, distance, page);
    }

    @ApiOperation("8、BITMAP-sign-签到")
    @GetMapping("/bitmap/sign")
    public String bitmapSign(Long userId) {
        bitMapService.sign(userId);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("9、BITMAP-count-连续签到次数统计")
    @GetMapping("/bitmap/count")
    public int bitmapCount(Long userId) {
        return bitMapService.count(userId);
    }

    @ApiOperation("10、HyperLogLog-add-添加当日UV数")
    @GetMapping("/hyperLogLog/add")
    public String hyperLogLogAdd(Long userId) {
        hyperLogLogService.add(userId);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("11、HyperLogLog-count-获取当日UV数")
    @GetMapping("/hyperLogLog/count")
    public Long hyperLogLogCount() {
        return hyperLogLogService.count();
    }

    @ApiOperation("12、SCAN--遍历查找BigKey")
    @GetMapping("/scan/bigKey")
    public String scanBigKey() {
        redisService.scanBigKey();
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("13、Pipeline")
    @GetMapping("/pipeline")
    public String pipeline() {
        redisService.pipeline();
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("14、计算key的slot")
    @GetMapping("/calculateSlot")
    public int calculateSlot(String key) {
        return ClusterSlotHashUtil.calculateSlot(key);
    }
}
